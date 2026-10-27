import {AlertDialog} from "radix-ui";
import {Trash2} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";

type Props = {
  title: string;            // π.χ. "Delete tour?"
  description: string;      // π.χ. "This cannot be undone."
  onConfirm: () => void;    // τρέχει ΜΟΝΟ αν ο χρήστης πατήσει "Delete"
  label?: string;           // aria-label του κουμπιού
}

// Κουμπί-σκουπιδάκι που ανοίγει παράθυρο επιβεβαίωσης (AlertDialog του Radix, όπως στο shadcn)
const ConfirmDeleteButton = ({title, description, onConfirm, label = "Delete"}: Props) => {
  return (
    <AlertDialog.Root>
      <AlertDialog.Trigger asChild>
        <Button variant="outline" size="icon" aria-label={label}>
          <Trash2 className="size-4"/>
        </Button>
      </AlertDialog.Trigger>
      <AlertDialog.Portal>
        <AlertDialog.Overlay className="fixed inset-0 z-50 bg-black/50"/>
        <AlertDialog.Content
          className="fixed left-1/2 top-1/2 z-50 w-[90vw] max-w-md -translate-x-1/2 -translate-y-1/2 space-y-4 rounded-2xl bg-background p-6 shadow-lg">
          <AlertDialog.Title className="text-lg font-semibold">{title}</AlertDialog.Title>
          <AlertDialog.Description className="text-sm text-muted-foreground">
            {description}
          </AlertDialog.Description>
          <div className="flex justify-end gap-2">
            <AlertDialog.Cancel asChild>
              <Button variant="outline">Cancel</Button>
            </AlertDialog.Cancel>
            <AlertDialog.Action asChild>
              <Button variant="destructive" onClick={onConfirm}>Delete</Button>
            </AlertDialog.Action>
          </div>
        </AlertDialog.Content>
      </AlertDialog.Portal>
    </AlertDialog.Root>
  )
}
export default ConfirmDeleteButton;
