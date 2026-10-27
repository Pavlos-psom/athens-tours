import {Link, useNavigate} from "react-router";
import {Pencil} from "lucide-react";
import {toast} from "sonner";
import PageHeader from "@/components/PageHeader.tsx";
import LoadingState from "@/components/LoadingState.tsx";
import ErrorState from "@/components/ErrorState.tsx";
import EmptyState from "@/components/EmptyState.tsx";
import ConfirmDeleteButton from "@/components/ConfirmDeleteButton.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {useFetch} from "@/hooks/useFetch.ts";
import {deleteSchedule, getSchedules} from "@/api/schedules.ts";
import {formatDateTime} from "@/utils/format.ts";
import {getErrorMessage} from "@/utils/errors.ts";

const AdminSchedulesPage = () => {
  const navigate = useNavigate();
  const {data: schedules, loading, error, reload} = useFetch(() => getSchedules());

  const handleDelete = async (uuid: string) => {
    try {
      await deleteSchedule(uuid);
      toast.success("Schedule deleted");
      reload();
    } catch (err) {
      toast.error(getErrorMessage(err, "Could not delete schedule"));
    }
  }

  if (loading) return <LoadingState/>;
  if (error) return <ErrorState message={error} onRetry={reload}/>;

  return (
    <div>
      <PageHeader
        title="Manage schedules"
        action={<Button asChild><Link to="/admin/schedules/new">+ New schedule</Link></Button>}
      />
      {!schedules || schedules.length === 0 ? (
        <EmptyState message="No schedules yet."/>
      ) : (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Tour</TableHead>
              <TableHead>Starts at</TableHead>
              <TableHead>Guide</TableHead>
              <TableHead>Capacity</TableHead>
              <TableHead className="text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {schedules.map((schedule) => (
              <TableRow key={schedule.uuid}>
                <TableCell className="font-medium">{schedule.tour.name}</TableCell>
                <TableCell>{formatDateTime(schedule.startsAt)}</TableCell>
                <TableCell>{schedule.tourGuide.username}</TableCell>
                <TableCell>{schedule.capacity}</TableCell>
                <TableCell className="space-x-2 text-right">
                  <Button variant="outline" size="icon" aria-label="Edit"
                          onClick={() => navigate(`/admin/schedules/${schedule.uuid}`)}>
                    <Pencil className="size-4"/>
                  </Button>
                  <ConfirmDeleteButton
                    title="Delete this schedule?"
                    description={`${schedule.tour.name} on ${formatDateTime(schedule.startsAt)} will be deleted.`}
                    onConfirm={() => handleDelete(schedule.uuid)}
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
export default AdminSchedulesPage;
