import {z} from "zod";

// Μία φόρμα για create ΚΑΙ edit. Στο edit το tour δεν αλλάζει, οπότε το tourUuid
// ελέγχεται "με το χέρι" στο onSubmit μόνο όταν δημιουργούμε νέο.
export const scheduleSchema = z.object({
  tourUuid: z.string(),
  tourGuideUuid: z.string().min(1, {error: "Please select a guide"}),
  startsAt: z.string().min(1, {error: "Date and time is required"}),
  capacity: z
    .number({error: "Capacity is required"})
    .int({error: "Capacity must be a whole number"})
    .positive({error: "Capacity must be positive"}),
})
export type ScheduleFormData = z.infer<typeof scheduleSchema>
