import type {ReactNode} from "react";

type Props = {
  title: string;
  subtitle?: string;
  action?: ReactNode; // π.χ. κουμπί "+ New tour" στα δεξιά
}

const PageHeader = ({title, subtitle, action}: Props) => {
  return (
    <div className="mb-6 flex flex-wrap items-center justify-between gap-4">
      <div>
        <h1 className="text-2xl font-bold">{title}</h1>
        {subtitle && <p className="text-muted-foreground">{subtitle}</p>}
      </div>
      {action}
    </div>
  )
}
export default PageHeader;
