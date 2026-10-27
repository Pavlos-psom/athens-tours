import {Link} from "react-router";
import {Card, CardContent, CardFooter, CardTitle} from "@/components/ui/card.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import {Button} from "@/components/ui/button.tsx";
import {formatDuration, formatPrice} from "@/utils/format.ts";
import type {Tour} from "@/types.ts";

const TourCard = ({tour}: { tour: Tour }) => {
  return (
    <Card>
      <div className="flex items-start justify-between gap-2">
        <CardTitle>{tour.name}</CardTitle>
        <Badge>{tour.category.name}</Badge>
      </div>
      <CardContent className="space-y-2">
        <p className="line-clamp-3 text-muted-foreground">{tour.description || "No description."}</p>
        <p>{formatDuration(tour.durationMinutes)}</p>
      </CardContent>
      <CardFooter>
        <span className="text-lg font-semibold">{formatPrice(tour.price)}</span>
        <Button asChild size="sm">
          <Link to={`/tours/${tour.uuid}`}>View details</Link>
        </Button>
      </CardFooter>
    </Card>
  )
}
export default TourCard;
