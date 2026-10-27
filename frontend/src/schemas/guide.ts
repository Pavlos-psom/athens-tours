import {z} from "zod";

// Στη φόρμα οι γλώσσες γράφονται ως ένα κείμενο "English, Greek" και πριν σταλούν
// μετατρέπονται σε πίνακα (βλ. parseLanguages στο utils/format.ts).
const phone = z.string().max(30, {error: "Phone must be at most 30 characters"});
const bio = z.string().max(1000, {error: "Bio must be at most 1000 characters"});
const languages = z.string().min(1, {error: "Enter at least one language (comma separated)"});

export const guideCreateSchema = z.object({
  username: z
    .string()
    .min(3, {error: "Username must be at least 3 characters"})
    .max(100, {error: "Username must be at most 100 characters"}),
  password: z
    .string()
    .regex(/^(?=.*[A-Za-z])(?=.*\d).{8,}$/, {
      error: "Password must be at least 8 characters and contain a letter and a digit",
    }),
  phone,
  bio,
  languages,
})
export type GuideCreateFormData = z.infer<typeof guideCreateSchema>

export const guideUpdateSchema = z.object({phone, bio, languages})
export type GuideUpdateFormData = z.infer<typeof guideUpdateSchema>
