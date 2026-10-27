import {z} from "zod";

export const loginSchema = z.object({
  username: z.string().min(1, {error: "Username is required"}),
  password: z.string().min(1, {error: "Password is required"}),
})
export type LoginFields = z.infer<typeof loginSchema>

// Ίδιοι κανόνες με το backend (UserInsertDTO): username 3-100, password >= 8 με γράμμα ΚΑΙ ψηφίο
export const registerSchema = z
  .object({
    username: z
      .string()
      .min(3, {error: "Username must be at least 3 characters"})
      .max(100, {error: "Username must be at most 100 characters"}),
    password: z
      .string()
      .regex(/^(?=.*[A-Za-z])(?=.*\d).{8,}$/, {
        error: "Password must be at least 8 characters and contain a letter and a digit",
      }),
    confirmPassword: z.string().min(1, {error: "Please confirm your password"}),
  })
  .refine((data) => data.password === data.confirmPassword, {
    error: "Passwords do not match",
    path: ["confirmPassword"],
  })
export type RegisterFields = z.infer<typeof registerSchema>
