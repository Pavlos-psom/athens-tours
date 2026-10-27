import {useNavigate, useParams} from "react-router";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {toast} from "sonner";
import LoadingState from "@/components/LoadingState.tsx";
import ErrorState from "@/components/ErrorState.tsx";
import {Field, FieldDescription, FieldError, FieldLabel} from "@/components/ui/field.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Textarea} from "@/components/ui/textarea.tsx";
import {Button} from "@/components/ui/button.tsx";
import {useFetch} from "@/hooks/useFetch.ts";
import {createGuide, getGuide, updateGuide} from "@/api/guides.ts";
import {
  type GuideCreateFormData,
  guideCreateSchema,
  type GuideUpdateFormData,
  guideUpdateSchema,
} from "@/schemas/guide.ts";
import {parseLanguages} from "@/utils/format.ts";
import {getErrorMessage} from "@/utils/errors.ts";
import type {TourGuide} from "@/types.ts";

// Μία σελίδα, δύο διαφορετικές φόρμες: Create (έχει username/password) και Edit (δεν έχει).
const GuideFormPage = () => {
  const {uuid} = useParams();
  const guideState = useFetch<TourGuide | null>(() => (uuid ? getGuide(uuid) : Promise.resolve(null)), [uuid]);

  if (guideState.loading) return <LoadingState/>;
  if (guideState.error) return <ErrorState message={guideState.error}/>;

  return guideState.data ? <GuideEditForm guide={guideState.data}/> : <GuideCreateForm/>;
}

const GuideCreateForm = () => {
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: {errors, isSubmitting},
  } = useForm<GuideCreateFormData>({
    resolver: zodResolver(guideCreateSchema),
    defaultValues: {username: "", password: "", phone: "", bio: "", languages: ""},
  })

  const onSubmit = async (data: GuideCreateFormData) => {
    try {
      await createGuide({...data, languages: parseLanguages(data.languages)});
      toast.success("Guide created");
      navigate("/admin/guides");
    } catch (err) {
      toast.error(getErrorMessage(err, "Could not create guide"));
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} autoComplete="off"
          className="mx-auto max-w-xl space-y-5 rounded-2xl border bg-card p-8">
      <h1 className="text-xl font-bold">New guide</h1>

      <Field>
        <FieldLabel htmlFor="username">Username</FieldLabel>
        <Input id="username" {...register("username")}/>
        <FieldError>{errors.username?.message}</FieldError>
      </Field>
      <Field>
        <FieldLabel htmlFor="password">Password</FieldLabel>
        <Input id="password" type="password" autoComplete="new-password" {...register("password")}/>
        <FieldError>{errors.password?.message}</FieldError>
      </Field>
      <Field>
        <FieldLabel htmlFor="phone">Phone</FieldLabel>
        <Input id="phone" {...register("phone")}/>
        <FieldError>{errors.phone?.message}</FieldError>
      </Field>
      <Field>
        <FieldLabel htmlFor="bio">Bio</FieldLabel>
        <Textarea id="bio" rows={4} {...register("bio")}/>
        <FieldError>{errors.bio?.message}</FieldError>
      </Field>
      <Field>
        <FieldLabel htmlFor="languages">Languages</FieldLabel>
        <Input id="languages" placeholder="English, Greek" {...register("languages")}/>
        <FieldDescription>Separate languages with commas.</FieldDescription>
        <FieldError>{errors.languages?.message}</FieldError>
      </Field>

      <div className="flex gap-3">
        <Button type="button" variant="outline" onClick={() => navigate("/admin/guides")}>Cancel</Button>
        <Button type="submit" disabled={isSubmitting} className="flex-1">
          {isSubmitting ? "Saving..." : "Create"}
        </Button>
      </div>
    </form>
  )
}

const GuideEditForm = ({guide}: { guide: TourGuide }) => {
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: {errors, isSubmitting},
  } = useForm<GuideUpdateFormData>({
    resolver: zodResolver(guideUpdateSchema),
    defaultValues: {
      phone: guide.phone ?? "",
      bio: guide.bio ?? "",
      languages: guide.languages.join(", "),
    },
  })

  const onSubmit = async (data: GuideUpdateFormData) => {
    try {
      await updateGuide(guide.uuid, {...data, languages: parseLanguages(data.languages)});
      toast.success("Guide updated");
      navigate("/admin/guides");
    } catch (err) {
      toast.error(getErrorMessage(err, "Could not update guide"));
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} autoComplete="off"
          className="mx-auto max-w-xl space-y-5 rounded-2xl border bg-card p-8">
      <h1 className="text-xl font-bold">Edit guide: {guide.username}</h1>

      <Field>
        <FieldLabel htmlFor="phone">Phone</FieldLabel>
        <Input id="phone" {...register("phone")}/>
        <FieldError>{errors.phone?.message}</FieldError>
      </Field>
      <Field>
        <FieldLabel htmlFor="bio">Bio</FieldLabel>
        <Textarea id="bio" rows={4} {...register("bio")}/>
        <FieldError>{errors.bio?.message}</FieldError>
      </Field>
      <Field>
        <FieldLabel htmlFor="languages">Languages</FieldLabel>
        <Input id="languages" placeholder="English, Greek" {...register("languages")}/>
        <FieldDescription>Separate languages with commas.</FieldDescription>
        <FieldError>{errors.languages?.message}</FieldError>
      </Field>

      <div className="flex gap-3">
        <Button type="button" variant="outline" onClick={() => navigate("/admin/guides")}>Cancel</Button>
        <Button type="submit" disabled={isSubmitting} className="flex-1">
          {isSubmitting ? "Saving..." : "Update"}
        </Button>
      </div>
    </form>
  )
}
export default GuideFormPage;
