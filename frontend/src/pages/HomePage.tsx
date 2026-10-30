import {Link} from "react-router";
import {Button} from "@/components/ui/button.tsx";
import {getCategories} from "@/api/tours.ts";
import {useFetch} from "@/hooks/useFetch.ts";

// Φωτογραφία ανά κατηγορία. Το κλειδί είναι το όνομα της κατηγορίας όπως είναι στη βάση.
const categoryImages: Record<string, string> = {
  "Food Tour": "/images/categories/food.png",
  "Ancient City Tour": "/images/categories/ancientcity.png",
  "Athens Downtown": "/images/categories/citycentre.png",
  "Pub Tour": "/images/categories/pub.png",
  "Alternative Athens": "/images/categories/alternative.png",
  "Outside Athens Tour": "/images/categories/outside.png",
  "Athens Seaside Tour": "/images/categories/riviera.png",
};

const HomePage = () => {
  const {data: categories} = useFetch(() => getCategories());

  return (
    <div className="space-y-10">
      <section
        className="rounded-3xl bg-aegean bg-cover bg-center px-8 py-24 text-center text-white"
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
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            {categories.map((category) => {
              const image = categoryImages[category.name];
              return (
                <Link
                  key={category.id}
                  to={`/tours?categoryId=${category.id}`}
                  className="flex h-48 items-end rounded-2xl bg-aegean bg-cover bg-center p-4 shadow transition-transform hover:scale-105"
                  style={image
                    ? {backgroundImage: `linear-gradient(to top, rgba(8,47,79,0.8), rgba(8,47,79,0.1)), url('${image}')`}
                    : undefined}
                >
                  <span className="text-lg font-semibold text-white drop-shadow-md">{category.name}</span>
                </Link>
              );
            })}
          </div>
        </section>
      )}
    </div>
  )
}
export default HomePage;