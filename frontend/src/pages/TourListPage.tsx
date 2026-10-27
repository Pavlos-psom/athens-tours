import {useSearchParams} from "react-router";
import {Button} from "@/components/ui/button.tsx";
import PageHeader from "@/components/PageHeader.tsx";
import TourCard from "@/components/TourCard.tsx";
import LoadingState from "@/components/LoadingState.tsx";
import ErrorState from "@/components/ErrorState.tsx";
import EmptyState from "@/components/EmptyState.tsx";
import {useFetch} from "@/hooks/useFetch.ts";
import {getCategories, getTours} from "@/api/tours.ts";

const TourListPage = () => {
  // Το φίλτρο ζει στο URL: /tours?categoryId=2  (έτσι δουλεύει και το refresh / το link)
  const [searchParams, setSearchParams] = useSearchParams();
  const categoryParam = searchParams.get("categoryId");
  const categoryId = categoryParam ? Number(categoryParam) : undefined;

  const {data: categories} = useFetch(() => getCategories());
  const {data: tours, loading, error, reload} = useFetch(() => getTours(categoryId), [categoryId]);

  return (
    <div>
      <PageHeader title="Tours" subtitle="Choose your next Athens experience"/>

      <div className="mb-6 flex flex-wrap gap-2">
        <Button
          size="sm"
          variant={categoryId === undefined ? "default" : "outline"}
          onClick={() => setSearchParams({})}
        >
          All
        </Button>
        {categories?.map((category) => (
          <Button
            key={category.id}
            size="sm"
            variant={categoryId === category.id ? "default" : "outline"}
            onClick={() => setSearchParams({categoryId: String(category.id)})}
          >
            {category.name}
          </Button>
        ))}
      </div>

      {loading && !tours ? (
        <LoadingState/>
      ) : error ? (
        <ErrorState message={error} onRetry={reload}/>
      ) : tours && tours.length > 0 ? (
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {tours.map((tour) => (
            <TourCard key={tour.uuid} tour={tour}/>
          ))}
        </div>
      ) : (
        <EmptyState message="No tours found."/>
      )}
    </div>
  )
}
export default TourListPage;
