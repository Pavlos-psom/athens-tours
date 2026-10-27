import {useEffect, useState} from "react";
import type {DependencyList} from "react";
import {getErrorMessage} from "@/utils/errors.ts";

// Custom hook: κάνει φόρτωση δεδομένων και δίνει { data, loading, error, reload }.
// - fetcher: η συνάρτηση που κάνει το request (π.χ. () => getTours())
// - deps:    πότε να ξανατρέξει (όπως το dependency array του useEffect)
export function useFetch<T>(fetcher: () => Promise<T>, deps: DependencyList = []) {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [reloadKey, setReloadKey] = useState(0);

  useEffect(() => {
    let ignore = false; // αν το component φύγει/αλλάξουν τα deps, αγνοούμε την παλιά απάντηση

    fetcher()
      .then((result) => {
        if (ignore) return;
        setData(result);
        setError(null);
      })
      .catch((err) => {
        if (!ignore) setError(getErrorMessage(err));
      })
      .finally(() => {
        if (!ignore) setLoading(false);
      });

    return () => {
      ignore = true;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [...deps, reloadKey]);

  const reload = () => setReloadKey((k) => k + 1);

  return {data, loading, error, reload};
}
