import {Link} from "react-router";
import {Button} from "@/components/ui/button.tsx";

const NotFoundPage = () => (
  <div className="space-y-4 py-16 text-center">
    <h1 className="text-4xl font-bold">404</h1>
    <p className="text-muted-foreground">This page does not exist.</p>
    <Button asChild><Link to="/">Back to home</Link></Button>
  </div>
)
export default NotFoundPage;
