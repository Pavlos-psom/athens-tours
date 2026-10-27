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
import {deleteTour, getTours} from "@/api/tours.ts";
import {formatDuration, formatPrice} from "@/utils/format.ts";
import {getErrorMessage} from "@/utils/errors.ts";

const AdminToursPage = () => {
  const navigate = useNavigate();
  const {data: tours, loading, error, reload} = useFetch(() => getTours());

  const handleDelete = async (uuid: string) => {
    try {
      await deleteTour(uuid);
      toast.success("Tour deleted");
      reload();
    } catch (err) {
      toast.error(getErrorMessage(err, "Could not delete tour"));
    }
  }

  if (loading) return <LoadingState/>;
  if (error) return <ErrorState message={error} onRetry={reload}/>;

  return (
    <div>
      <PageHeader
        title="Manage tours"
        action={<Button asChild><Link to="/admin/tours/new">+ New tour</Link></Button>}
      />
      {!tours || tours.length === 0 ? (
        <EmptyState message="No tours yet."/>
      ) : (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Name</TableHead>
              <TableHead>Category</TableHead>
              <TableHead>Price</TableHead>
              <TableHead>Duration</TableHead>
              <TableHead className="text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {tours.map((tour) => (
              <TableRow key={tour.uuid}>
                <TableCell className="font-medium">{tour.name}</TableCell>
                <TableCell>{tour.category.name}</TableCell>
                <TableCell>{formatPrice(tour.price)}</TableCell>
                <TableCell>{formatDuration(tour.durationMinutes)}</TableCell>
                <TableCell className="space-x-2 text-right">
                  <Button variant="outline" size="icon" aria-label="Edit"
                          onClick={() => navigate(`/admin/tours/${tour.uuid}`)}>
                    <Pencil className="size-4"/>
                  </Button>
                  <ConfirmDeleteButton
                    title="Delete this tour?"
                    description={`"${tour.name}" will be permanently deleted.`}
                    onConfirm={() => handleDelete(tour.uuid)}
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
export default AdminToursPage;
