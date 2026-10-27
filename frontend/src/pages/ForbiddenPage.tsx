import {Link} from "react-router";
import {Button} from "@/components/ui/button.tsx";

const ForbiddenPage = () => (
  <div className="space-y-4 py-16 text-center">
    <h1 className="text-4xl font-bold">403</h1>
    <p className="text-muted-foreground">You do not have permission to view this page.</p>
    <Button asChild><Link to="/">Back to home</Link></Button>
  </div>
)
export default ForbiddenPage;
