import {Link, useNavigate} from "react-router";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {toast} from "sonner";
import {Field, FieldError, FieldLabel} from "@/components/ui/field.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {type RegisterFields, registerSchema} from "@/schemas/auth.ts";
import {register as registerUser} from "@/api/auth.ts";
import {getErrorMessage} from "@/utils/errors.ts";

export default function RegisterPage() {
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: {errors, isSubmitting},
  } = useForm<RegisterFields>({
    resolver: zodResolver(registerSchema),
  })

  const onSubmit = async (data: RegisterFields) => {
    try {
      await registerUser(data);
      toast.success("Account created! You can log in now.");
      navigate("/login");
    } catch (error) {
      // π.χ. 409 UserAlreadyExists
      toast.error(getErrorMessage(error, "Registration failed"));
    }
  }

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="mx-auto max-w-sm space-y-6 rounded-2xl border bg-card p-8 shadow"
    >
      <h1 className="mb-2 text-center text-2xl font-bold">Create account</h1>
      <Field>
        <FieldLabel htmlFor="username">Username</FieldLabel>
        <Input id="username" autoComplete="username" {...register("username")}/>
        <FieldError>{errors.username?.message}</FieldError>
      </Field>
      <Field>
        <FieldLabel htmlFor="password">Password</FieldLabel>
        <Input id="password" type="password" autoComplete="new-password" {...register("password")}/>
        <FieldError>{errors.password?.message}</FieldError>
      </Field>
      <Field>
        <FieldLabel htmlFor="confirmPassword">Confirm password</FieldLabel>
        <Input id="confirmPassword" type="password" autoComplete="new-password" {...register("confirmPassword")}/>
        <FieldError>{errors.confirmPassword?.message}</FieldError>
      </Field>
      <Button type="submit" className="w-full" disabled={isSubmitting}>
        {isSubmitting ? "Creating..." : "Register"}
      </Button>
      <p className="text-center text-sm text-muted-foreground">
        Already registered? <Link to="/login" className="text-primary hover:underline">Login</Link>
      </p>
    </form>
  )
}
