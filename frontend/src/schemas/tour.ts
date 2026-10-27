import {z} from "zod";

// Τα number πεδία έρχονται από <input> ως string. Με { valueAsNumber: true } στο register()
// τα παίρνουμε ήδη ως number (ή NaN αν το πεδίο είναι κενό – τότε πετυχαίνει το "error" του z.number).
export const tourSchema = z.object({
  name: z
    .string()
    .min(2, {error: "Name must be at least 2 characters"})
    .max(150, {error: "Name must be at most 150 characters"}),
  description: z.string().max(2000, {error: "Description must be at most 2000 characters"}),
  price: z
    .number({error: "Price is required"})
    .min(0.01, {error: "Price must be greater than zero"}),
  durationMinutes: z
    .number({error: "Duration is required"})
    .int({error: "Duration must be a whole number"})
    .positive({error: "Duration must be positive"}),
  categoryId: z
    .number({error: "Please select a category"})
    .int()
    .positive({error: "Please select a category"}),
})
export type TourFormData = z.infer<typeof tourSchema>
