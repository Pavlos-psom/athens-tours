import PageHeader from "@/components/PageHeader.tsx";
import LoadingState from "@/components/LoadingState.tsx";
import ErrorState from "@/components/ErrorState.tsx";
import EmptyState from "@/components/EmptyState.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import {Card, CardContent, CardTitle} from "@/components/ui/card.tsx";
import {useFetch} from "@/hooks/useFetch.ts";
import {getGuides} from "@/api/guides.ts";

const GuideListPage = () => {
  const {data: guides, loading, error, reload} = useFetch(() => getGuides());

  if (loading) return <LoadingState/>;
  if (error) return <ErrorState message={error} onRetry={reload}/>;

  return (
    <div>
      <PageHeader title="Our guides" subtitle="Meet the people who will show you Athens"/>
      {!guides || guides.length === 0 ? (
        <EmptyState message="No guides yet."/>
      ) : (
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {guides.map((guide) => (
            <Card key={guide.uuid}>
              <CardTitle>{guide.username}</CardTitle>
              <CardContent className="space-y-3">
                <p className="text-muted-foreground">{guide.bio || "No bio."}</p>
                {guide.phone && <p>Phone: {guide.phone}</p>}
                <div className="flex flex-wrap gap-2">
                  {guide.languages.map((language) => (
                    <Badge key={language}>{language}</Badge>
                  ))}
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}
export default GuideListPage;
