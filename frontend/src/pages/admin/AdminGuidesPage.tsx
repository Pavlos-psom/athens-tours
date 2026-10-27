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
import {deleteGuide, getGuides} from "@/api/guides.ts";
import {getErrorMessage} from "@/utils/errors.ts";

const AdminGuidesPage = () => {
  const navigate = useNavigate();
  const {data: guides, loading, error, reload} = useFetch(() => getGuides());

  const handleDelete = async (uuid: string) => {
    try {
      await deleteGuide(uuid);
      toast.success("Guide deleted");
      reload();
    } catch (err) {
      toast.error(getErrorMessage(err, "Could not delete guide"));
    }
  }

  if (loading) return <LoadingState/>;
  if (error) return <ErrorState message={error} onRetry={reload}/>;

  return (
    <div>
      <PageHeader
        title="Manage guides"
        action={<Button asChild><Link to="/admin/guides/new">+ New guide</Link></Button>}
      />
      {!guides || guides.length === 0 ? (
        <EmptyState message="No guides yet."/>
      ) : (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Username</TableHead>
              <TableHead>Phone</TableHead>
              <TableHead>Languages</TableHead>
              <TableHead className="text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {guides.map((guide) => (
              <TableRow key={guide.uuid}>
                <TableCell className="font-medium">{guide.username}</TableCell>
                <TableCell>{guide.phone ?? "-"}</TableCell>
                <TableCell>{guide.languages.join(", ")}</TableCell>
                <TableCell className="space-x-2 text-right">
                  <Button variant="outline" size="icon" aria-label="Edit"
                          onClick={() => navigate(`/admin/guides/${guide.uuid}`)}>
                    <Pencil className="size-4"/>
                  </Button>
                  <ConfirmDeleteButton
                    title="Delete this guide?"
                    description={`Guide "${guide.username}" will be permanently deleted.`}
                    onConfirm={() => handleDelete(guide.uuid)}
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
export default AdminGuidesPage;
