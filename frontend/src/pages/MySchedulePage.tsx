import PageHeader from "@/components/PageHeader.tsx";
import LoadingState from "@/components/LoadingState.tsx";
import ErrorState from "@/components/ErrorState.tsx";
import EmptyState from "@/components/EmptyState.tsx";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {useFetch} from "@/hooks/useFetch.ts";
import {getMySchedule} from "@/api/schedules.ts";
import {formatDateTime, formatDuration} from "@/utils/format.ts";

const MySchedulePage = () => {
  const {data: schedules, loading, error, reload} = useFetch(() => getMySchedule());

  if (loading) return <LoadingState/>;
  if (error) return <ErrorState message={error} onRetry={reload}/>;

  return (
    <div>
      <PageHeader title="My schedule" subtitle="Tours you are guiding"/>
      {!schedules || schedules.length === 0 ? (
        <EmptyState message="You have no scheduled tours."/>
      ) : (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Tour</TableHead>
              <TableHead>Starts at</TableHead>
              <TableHead>Duration</TableHead>
              <TableHead>Capacity</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {schedules.map((schedule) => (
              <TableRow key={schedule.uuid}>
                <TableCell className="font-medium">{schedule.tour.name}</TableCell>
                <TableCell>{formatDateTime(schedule.startsAt)}</TableCell>
                <TableCell>{formatDuration(schedule.tour.durationMinutes)}</TableCell>
                <TableCell>{schedule.capacity}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}
    </div>
  )
}
export default MySchedulePage;
