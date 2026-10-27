import PageHeader from "@/components/PageHeader.tsx";
import LoadingState from "@/components/LoadingState.tsx";
import ErrorState from "@/components/ErrorState.tsx";
import EmptyState from "@/components/EmptyState.tsx";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {useFetch} from "@/hooks/useFetch.ts";
import {getAllBookings} from "@/api/bookings.ts";
import {formatDateTime} from "@/utils/format.ts";

const AdminBookingsPage = () => {
  const {data: bookings, loading, error, reload} = useFetch(() => getAllBookings());

  if (loading) return <LoadingState/>;
  if (error) return <ErrorState message={error} onRetry={reload}/>;

  return (
    <div>
      <PageHeader title="All bookings"/>
      {!bookings || bookings.length === 0 ? (
        <EmptyState message="No bookings yet."/>
      ) : (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Customer</TableHead>
              <TableHead>Tour</TableHead>
              <TableHead>Starts at</TableHead>
              <TableHead>Guide</TableHead>
              <TableHead>Booked at</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {bookings.map((booking) => (
              <TableRow key={booking.uuid}>
                <TableCell className="font-medium">{booking.customerUsername}</TableCell>
                <TableCell>{booking.schedule.tour.name}</TableCell>
                <TableCell>{formatDateTime(booking.schedule.startsAt)}</TableCell>
                <TableCell>{booking.schedule.tourGuide.username}</TableCell>
                <TableCell>{formatDateTime(booking.bookedAt)}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}
    </div>
  )
}
export default AdminBookingsPage;
