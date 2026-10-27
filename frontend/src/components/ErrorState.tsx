import {Button} from "@/components/ui/button.tsx";

type Props = {
  message: string;
  onRetry?: () => void;
}

const ErrorState = ({message, onRetry}: Props) => (
  <div className="mx-auto max-w-md space-y-4 rounded-xl border border-destructive/40 p-6 text-center">
    <p className="text-destructive">{message}</p>
    {onRetry && <Button variant="outline" onClick={onRetry}>Try again</Button>}
  </div>
)
export default ErrorState;
