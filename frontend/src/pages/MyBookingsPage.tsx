import {toast} from "sonner";
import PageHeader from "@/components/PageHeader.tsx";
import LoadingState from "@/components/LoadingState.tsx";
import ErrorState from "@/components/ErrorState.tsx";
import EmptyState from "@/components/EmptyState.tsx";
import ConfirmDeleteButton from "@/components/ConfirmDeleteButton.tsx";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {useFetch} from "@/hooks/useFetch.ts";
import {cancelBooking, getMyBookings} from "@/api/bookings.ts";
import {formatDateTime} from "@/utils/format.ts";
import {getErrorMessage} from "@/utils/errors.ts";

const MyBookingsPage = () => {
  const {data: bookings, loading, error, reload} = useFetch(() => getMyBookings());

  const handleCancel = async (uuid: string) => {
    try {
      await cancelBooking(uuid);
      toast.success("Booking cancelled");
      reload(); // ξαναφόρτωσε τη λίστα
    } catch (err) {
      toast.error(getErrorMessage(err, "Could not cancel booking"));
    }
  }

  if (loading) return <LoadingState/>;
  if (error) return <ErrorState message={error} onRetry={reload}/>;

  return (
    <div>
      <PageHeader title="My bookings"/>
      {!bookings || bookings.length === 0 ? (
        <EmptyState message="You have no bookings yet."/>
      ) : (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Tour</TableHead>
              <TableHead>Starts at</TableHead>
              <TableHead>Guide</TableHead>
              <TableHead>Booked at</TableHead>
              <TableHead className="text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {bookings.map((booking) => (
              <TableRow key={booking.uuid}>
                <TableCell className="font-medium">{booking.schedule.tour.name}</TableCell>
                <TableCell>{formatDateTime(booking.schedule.startsAt)}</TableCell>
                <TableCell>{booking.schedule.tourGuide.username}</TableCell>
                <TableCell>{formatDateTime(booking.bookedAt)}</TableCell>
                <TableCell className="text-right">
                  <ConfirmDeleteButton
                    label="Cancel booking"
                    title="Cancel this booking?"
                    description={`Your spot on "${booking.schedule.tour.name}" will be released.`}
                    onConfirm={() => handleCancel(booking.uuid)}
                  />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}
    </div>
  )
}
export default MyBookingsPage;
