import {apiFetch} from "@/api/client.ts";
import type {Category, Tour} from "@/types.ts";
import type {TourFormData} from "@/schemas/tour.ts";

export const getCategories = () => apiFetch<Category[]>("/categories");

export const getTours = (categoryId?: number) =>
  apiFetch<Tour[]>(categoryId ? `/tours?categoryId=${categoryId}` : "/tours");

export const getTour = (uuid: string) => apiFetch<Tour>(`/tours/${uuid}`);

export const createTour = (data: TourFormData) =>
  apiFetch<Tour>("/tours", {method: "POST", body: data, auth: true});

export const updateTour = (uuid: string, data: TourFormData) =>
  apiFetch<Tour>(`/tours/${uuid}`, {method: "PUT", body: data, auth: true});

export const deleteTour = (uuid: string) =>
  apiFetch<void>(`/tours/${uuid}`, {method: "DELETE", auth: true});
