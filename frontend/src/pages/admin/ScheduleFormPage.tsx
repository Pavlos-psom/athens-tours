import {useNavigate, useParams} from "react-router";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {toast} from "sonner";
import LoadingState from "@/components/LoadingState.tsx";
import ErrorState from "@/components/ErrorState.tsx";
import {Field, FieldError, FieldLabel} from "@/components/ui/field.tsx";
import {Input} from "@/components/ui/input.tsx";
import {NativeSelect} from "@/components/ui/native-select.tsx";
import {Button} from "@/components/ui/button.tsx";
import {useFetch} from "@/hooks/useFetch.ts";
import {getTours} from "@/api/tours.ts";
import {getGuides} from "@/api/guides.ts";
import {createSchedule, getSchedule, updateSchedule} from "@/api/schedules.ts";
import {type ScheduleFormData, scheduleSchema} from "@/schemas/schedule.ts";
import {toBackendDateTime, toDateTimeLocalValue} from "@/utils/format.ts";
import {getErrorMessage} from "@/utils/errors.ts";
import type {Tour, TourGuide, TourSchedule} from "@/types.ts";

const ScheduleFormPage = () => {
  const {uuid} = useParams();
  const toursState = useFetch(() => getTours());
  const guidesState = useFetch(() => getGuides());
  const scheduleState = useFetch<TourSchedule | null>(() => (uuid ? getSchedule(uuid) : Promise.resolve(null)), [uuid]);

  if (toursState.loading || guidesState.loading || scheduleState.loading) return <LoadingState/>;
  if (toursState.error || guidesState.error || scheduleState.error || !toursState.data || !guidesState.data) {
    return <ErrorState message={toursState.error ?? guidesState.error ?? scheduleState.error ?? "Failed to load"}/>;
  }

  return <ScheduleForm schedule={scheduleState.data} tours={toursState.data} guides={guidesState.data}/>;
}

type Props = {
  schedule: TourSchedule | null; // null = δημιουργία νέου
  tours: Tour[];
  guides: TourGuide[];
}

const ScheduleForm = ({schedule, tours, guides}: Props) => {
  const navigate = useNavigate();
  const isEdit = schedule !== null;

  const {
    register,
    handleSubmit,
    setError,
    formState: {errors, isSubmitting},
  } = useForm<ScheduleFormData>({
    resolver: zodResolver(scheduleSchema),
    defaultValues: schedule
      ? {
        tourUuid: schedule.tour.uuid,
        tourGuideUuid: schedule.tourGuide.uuid,
        startsAt: toDateTimeLocalValue(schedule.startsAt),
        capacity: schedule.capacity,
      }
      : {tourUuid: "", tourGuideUuid: "", startsAt: ""},
  })

  const onSubmit = async (data: ScheduleFormData) => {
    // Στο create πρέπει να έχει επιλεγεί tour (στο edit δεν αλλάζει)
    if (!schedule && !data.tourUuid) {
      setError("tourUuid", {message: "Please select a tour"});
      return;
    }

    const startsAt = toBackendDateTime(data.startsAt);

    try {
      if (schedule) {
        await updateSchedule(schedule.uuid, {
          tourGuideUuid: data.tourGuideUuid,
          startsAt,
          capacity: data.capacity,
        });
        toast.success("Schedule updated");
      } else {
        await createSchedule({
          tourUuid: data.tourUuid,
          tourGuideUuid: data.tourGuideUuid,
          startsAt,
          capacity: data.capacity,
        });
        toast.success("Schedule created");
      }
      navigate("/admin/schedules");
    } catch (err) {
      // π.χ. validation: "Start date/time must be in the future"
      toast.error(getErrorMessage(err, "Could not save schedule"));
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} autoComplete="off"
          className="mx-auto max-w-xl space-y-5 rounded-2xl border bg-card p-8">
      <h1 className="text-xl font-bold">{isEdit ? "Edit schedule" : "New schedule"}</h1>

      {schedule ? (
        <Field>
          <FieldLabel>Tour</FieldLabel>
          <p className="text-sm">{schedule.tour.name}</p>
        </Field>
      ) : (
        <Field>
          <FieldLabel htmlFor="tourUuid">Tour</FieldLabel>
          <NativeSelect id="tourUuid" {...register("tourUuid")}>
            <option value="">Select a tour...</option>
            {tours.map((tour) => (
              <option key={tour.uuid} value={tour.uuid}>{tour.name}</option>
            ))}
          </NativeSelect>
          <FieldError>{errors.tourUuid?.message}</FieldError>
        </Field>
      )}

      <Field>
        <FieldLabel htmlFor="tourGuideUuid">Guide</FieldLabel>
        <NativeSelect id="tourGuideUuid" {...register("tourGuideUuid")}>
          <option value="">Select a guide...</option>
          {guides.map((guide) => (
            <option key={guide.uuid} value={guide.uuid}>{guide.username}</option>
          ))}
        </NativeSelect>
        <FieldError>{errors.tourGuideUuid?.message}</FieldError>
      </Field>

      <Field>
        <FieldLabel htmlFor="startsAt">Starts at</FieldLabel>
        <Input id="startsAt" type="datetime-local" {...register("startsAt")}/>
        <FieldError>{errors.startsAt?.message}</FieldError>
      </Field>

      <Field>
        <FieldLabel htmlFor="capacity">Capacity</FieldLabel>
        <Input id="capacity" type="number" min={1} {...register("capacity", {valueAsNumber: true})}/>
        <FieldError>{errors.capacity?.message}</FieldError>
      </Field>

      <div className="flex gap-3">
        <Button type="button" variant="outline" onClick={() => navigate("/admin/schedules")}>Cancel</Button>
        <Button type="submit" disabled={isSubmitting} className="flex-1">
          {isSubmitting ? "Saving..." : isEdit ? "Update" : "Create"}
        </Button>
      </div>
    </form>
  )
}
export default ScheduleFormPage;
