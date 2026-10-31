import {Link, useLocation, useNavigate} from "react-router";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {toast} from "sonner";
import {Field, FieldError, FieldLabel} from "@/components/ui/field.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {type LoginFields, loginSchema} from "@/schemas/auth.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import {getErrorMessage} from "@/utils/errors.ts";
import type {Role} from "@/types.ts";

// Πού πηγαίνει ο καθένας μετά το login, αν δεν ερχόταν από κάποια συγκεκριμένη σελίδα
const homeByRole: Record<Role, string> = {
  ADMIN: "/admin",
  GUIDE: "/my-schedule",
  CUSTOMER: "/tours",
}

// Σε ποιον ρόλο ανήκει κάθε προστατευμένο κομμάτι της εφαρμογής (πρέπει να συμφωνεί με το App.tsx)
const roleByPrefix: Record<string, Role> = {
  "/admin": "ADMIN",
  "/my-schedule": "GUIDE",
  "/my-bookings": "CUSTOMER",
}

// true αν ο ρόλος επιτρέπεται στη διαδρομή (οι δημόσιες διαδρομές επιτρέπονται σε όλους)
const canVisit = (path: string, role: Role) => {
  const entry = Object.entries(roleByPrefix).find(([prefix]) => path.startsWith(prefix));
  return !entry || entry[1] === role;
}

export default function LoginPage() {
  const {loginUser} = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from: string | undefined = location.state?.from?.pathname;

  const {
    register,
    handleSubmit,
    formState: {errors, isSubmitting},
  } = useForm<LoginFields>({
    resolver: zodResolver(loginSchema),
  })

  const onSubmit = async (data: LoginFields) => {
    try {
      const user = await loginUser(data);
      toast.success("Login successful");
         const target = user && from && canVisit(from, user.role) ? from : user ? homeByRole[user.role] : "/";
      navigate(target, {replace: true});
    } catch (error) {
      toast.error(getErrorMessage(error, "Login failed"));
    }
  }

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="mx-auto max-w-sm space-y-6 rounded-2xl border bg-card p-8 shadow"
    >
      <h1 className="mb-2 text-center text-2xl font-bold">Login</h1>
      <Field>
        <FieldLabel htmlFor="username">Username</FieldLabel>
        <Input id="username" autoComplete="username" {...register("username")}/>
        <FieldError>{errors.username?.message}</FieldError>
      </Field>
      <Field>
        <FieldLabel htmlFor="password">Password</FieldLabel>
        <Input id="password" type="password" autoComplete="current-password" {...register("password")}/>
        <FieldError>{errors.password?.message}</FieldError>
      </Field>
      <Button type="submit" className="w-full" disabled={isSubmitting}>
        {isSubmitting ? "Logging in..." : "Login"}
      </Button>
      <p className="text-center text-sm text-muted-foreground">
        No account? <Link to="/register" className="text-primary hover:underline">Register</Link>
      </p>
    </form>
  )
}
