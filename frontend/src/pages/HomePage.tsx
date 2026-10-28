import {Link} from "react-router";
import {Button} from "@/components/ui/button.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import {getCategories} from "@/api/tours.ts";
import {useFetch} from "@/hooks/useFetch.ts";

const HomePage = () => {
  const {data: categories} = useFetch(() => getCategories());

  return (
    <div className="space-y-10">
      <section className="rounded-3xl bg-aegean bg-cover bg-center px-8 py-24 text-center text-white"
style={{backgroundImage: "linear-gradient(rgba(8,47,79,0.45), rgba(8,47,79,0.45)), url('/images/athens.png')"}}
>
        <h1 className="text-4xl font-bold drop-shadow-lg md:text-5xl">Discover Athens with local guides</h1>
        <p className="mx-auto mt-4 max-w-3xl text-xl font-semibold text-white drop-shadow-md md:text-2xl">
          From the Acropolis to hidden neighbourhoods – browse our tours, pick a date and book your spot in seconds.
        </p>
        <Button asChild size="lg" className="mt-8">
          <Link to="/tours">Browse tours</Link>
        </Button>
      </section>

      {categories && categories.length > 0 && (
        <section>
          <h2 className="mb-4 text-xl font-semibold">Tour categories</h2>
          <div className="flex flex-wrap gap-3">
            {categories.map((category) => (
              <Link key={category.id} to={`/tours?categoryId=${category.id}`}>
                <Badge variant="outline" className="px-4 py-1.5 text-sm hover:bg-muted">
                  {category.name}
                </Badge>
              </Link>
            ))}
          </div>
        </section>
      )}
    </div>
  )
}
export default HomePage;
