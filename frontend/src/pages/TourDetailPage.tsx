import {useState} from "react";
import {Link, useNavigate, useParams} from "react-router";
import {toast} from "sonner";
import {Button} from "@/components/ui/button.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import {Card} from "@/components/ui/card.tsx";
import LoadingState from "@/components/LoadingState.tsx";
import ErrorState from "@/components/ErrorState.tsx";
import EmptyState from "@/components/EmptyState.tsx";
import {useAuth} from "@/hooks/useAuth.ts";
import {useFetch} from "@/hooks/useFetch.ts";
import {getTour} from "@/api/tours.ts";
import {getSchedules} from "@/api/schedules.ts";
import {createBooking} from "@/api/bookings.ts";
import {formatDateTime, formatDuration, formatPrice} from "@/utils/format.ts";
import {getErrorMessage} from "@/utils/errors.ts";

const TourDetailPage = () => {
  const {uuid} = useParams();           // το :uuid από το URL /tours/:uuid
  const navigate = useNavigate();
  const {user} = useAuth();
  const [now] = useState(() => Date.now()); // "τώρα" – υπολογίζεται μία φορά

  const tourState = useFetch(() => getTour(uuid!), [uuid]);
  const schedulesState = useFetch(() => getSchedules(uuid), [uuid]);

  const handleBook = async (scheduleUuid: string) => {
    try {
      await createBooking(scheduleUuid);
      toast.success("Booking confirmed!");
      navigate("/my-bookings");
    } catch (err) {
      // π.χ. 400 ScheduleFull -> "Schedule is full"
      toast.error(getErrorMessage(err, "Booking failed"));
    }
  }

  if (tourState.loading) return <LoadingState/>;
  if (tourState.error || !tourState.data) {
    return <ErrorState message={tourState.error ?? "Tour not found"} onRetry={tourState.reload}/>;
  }

  const tour = tourState.data;
  const schedules = schedulesState.data ?? [];

  return (
    <div className="space-y-8">
      <div>
        <Link to="/tours" className="text-sm text-muted-foreground hover:underline">&larr; Back to tours</Link>
        <div className="mt-2 flex flex-wrap items-center gap-3">
          <h1 className="text-3xl font-bold">{tour.name}</h1>
          <Badge>{tour.category.name}</Badge>
        </div>
        <p className="mt-4 max-w-3xl whitespace-pre-line text-muted-foreground">
          {tour.description || "No description."}
        </p>
        <div className="mt-4 flex gap-6 text-lg">
          <span className="font-semibold">{formatPrice(tour.price)}</span>
          <span>{formatDuration(tour.durationMinutes)}</span>
        </div>
      </div>

      <section>
        <h2 className="mb-4 text-xl font-semibold">Available dates</h2>

        {schedulesState.loading ? (
          <LoadingState/>
        ) : schedulesState.error ? (
          <ErrorState message={schedulesState.error} onRetry={schedulesState.reload}/>
        ) : schedules.length === 0 ? (
          <EmptyState message="No dates scheduled for this tour yet."/>
        ) : (
          <div className="grid gap-4 md:grid-cols-2">
            {schedules.map((schedule) => {
              const isPast = new Date(schedule.startsAt).getTime() < now;
              return (
                <Card key={schedule.uuid}>
                  <div className="flex items-start justify-between gap-2">
                    <div>
                      <p className="font-semibold">{formatDateTime(schedule.startsAt)}</p>
                      <p className="text-sm text-muted-foreground">
                        Guide: {schedule.tourGuide.username} · Capacity: {schedule.capacity}
                      </p>
                    </div>
                    {isPast && <Badge variant="outline">Past</Badge>}
                  </div>

                  {!isPast && (
                    user?.role === "CUSTOMER" ? (
                      <Button onClick={() => handleBook(schedule.uuid)}>Book this date</Button>
                    ) : !user ? (
                      <Button asChild variant="outline">
                        <Link to="/login" state={{from: {pathname: `/tours/${tour.uuid}`}}}>Log in to book</Link>
                      </Button>
                    ) : null
                  )}
                </Card>
              )
            })}
          </div>
        )}
      </section>
    </div>
  )
}
export default TourDetailPage;
