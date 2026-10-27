import {useNavigate, useParams} from "react-router";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {toast} from "sonner";
import LoadingState from "@/components/LoadingState.tsx";
import ErrorState from "@/components/ErrorState.tsx";
import {Field, FieldError, FieldLabel} from "@/components/ui/field.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Textarea} from "@/components/ui/textarea.tsx";
import {NativeSelect} from "@/components/ui/native-select.tsx";
import {Button} from "@/components/ui/button.tsx";
import {useFetch} from "@/hooks/useFetch.ts";
import {createTour, getCategories, getTour, updateTour} from "@/api/tours.ts";
import {type TourFormData, tourSchema} from "@/schemas/tour.ts";
import {getErrorMessage} from "@/utils/errors.ts";
import type {Category, Tour} from "@/types.ts";

// ---- Εξωτερικό component: φορτώνει τα δεδομένα ----
const TourFormPage = () => {
  const {uuid} = useParams();   // υπάρχει μόνο στο /admin/tours/:uuid (edit)
  const catState = useFetch(() => getCategories());
  const tourState = useFetch<Tour | null>(() => (uuid ? getTour(uuid) : Promise.resolve(null)), [uuid]);

  if (catState.loading || tourState.loading) return <LoadingState/>;
  if (catState.error || tourState.error || !catState.data) {
    return <ErrorState message={catState.error ?? tourState.error ?? "Failed to load"}/>;
  }

  return <TourForm tour={tourState.data} categories={catState.data}/>;
}

// ---- Εσωτερικό component: η φόρμα. Μπαίνει στην οθόνη ΜΟΝΟ αφού έχουν φορτώσει τα δεδομένα,
// άρα τα defaultValues είναι έτοιμα από την αρχή. ----
const TourForm = ({tour, categories}: { tour: Tour | null; categories: Category[] }) => {
  const navigate = useNavigate();
  const isEdit = tour !== null;

  const {
    register,
    handleSubmit,
    formState: {errors, isSubmitting},
  } = useForm<TourFormData>({
    resolver: zodResolver(tourSchema),
    defaultValues: tour
      ? {
        name: tour.name,
        description: tour.description ?? "",
        price: tour.price,
        durationMinutes: tour.durationMinutes,
        categoryId: tour.category.id,
      }
      : {name: "", description: ""},
  })

  const onSubmit = async (data: TourFormData) => {
    try {
      if (tour) {
        await updateTour(tour.uuid, data);
        toast.success("Tour updated");
      } else {
        await createTour(data);
        toast.success("Tour created");
      }
      navigate("/admin/tours");
    } catch (err) {
      toast.error(getErrorMessage(err, "Could not save tour"));
    }
  }

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="mx-auto max-w-xl space-y-5 rounded-2xl border bg-card p-8"
      autoComplete="off"
    >
      <h1 className="text-xl font-bold">{isEdit ? "Edit tour" : "New tour"}</h1>

      <Field>
        <FieldLabel htmlFor="name">Name</FieldLabel>
        <Input id="name" {...register("name")}/>
        <FieldError>{errors.name?.message}</FieldError>
      </Field>

      <Field>
        <FieldLabel htmlFor="description">Description</FieldLabel>
        <Textarea id="description" rows={5} {...register("description")}/>
        <FieldError>{errors.description?.message}</FieldError>
      </Field>

      <Field>
        <FieldLabel htmlFor="categoryId">Category</FieldLabel>
        <NativeSelect id="categoryId" {...register("categoryId", {valueAsNumber: true})}>
          <option value="">Select a category...</option>
          {categories.map((category) => (
            <option key={category.id} value={category.id}>{category.name}</option>
          ))}
        </NativeSelect>
        <FieldError>{errors.categoryId?.message}</FieldError>
      </Field>

      <div className="grid gap-5 sm:grid-cols-2">
        <Field>
          <FieldLabel htmlFor="price">Price (EUR)</FieldLabel>
          <Input id="price" type="number" step="0.01" {...register("price", {valueAsNumber: true})}/>
          <FieldError>{errors.price?.message}</FieldError>
        </Field>
        <Field>
          <FieldLabel htmlFor="durationMinutes">Duration (minutes)</FieldLabel>
          <Input id="durationMinutes" type="number" {...register("durationMinutes", {valueAsNumber: true})}/>
          <FieldError>{errors.durationMinutes?.message}</FieldError>
        </Field>
      </div>

      <div className="flex gap-3">
        <Button type="button" variant="outline" onClick={() => navigate("/admin/tours")}>Cancel</Button>
        <Button type="submit" disabled={isSubmitting} className="flex-1">
          {isSubmitting ? "Saving..." : isEdit ? "Update" : "Create"}
        </Button>
      </div>
    </form>
  )
}
export default TourFormPage;
