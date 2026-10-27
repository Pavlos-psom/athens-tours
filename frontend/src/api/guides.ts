import {apiFetch} from "@/api/client.ts";
import type {TourGuide} from "@/types.ts";

// Τι στέλνουμε στο backend (languages = πίνακας, όχι κείμενο)
export type GuideCreatePayload = {
  username: string;
  password: string;
  phone: string;
  bio: string;
  languages: string[];
};
export type GuideUpdatePayload = Omit<GuideCreatePayload, "username" | "password">;

export const getGuides = () => apiFetch<TourGuide[]>("/tour-guides");

export const getGuide = (uuid: string) => apiFetch<TourGuide>(`/tour-guides/${uuid}`);

export const createGuide = (data: GuideCreatePayload) =>
  apiFetch<TourGuide>("/tour-guides", {method: "POST", body: data, auth: true});

export const updateGuide = (uuid: string, data: GuideUpdatePayload) =>
  apiFetch<TourGuide>(`/tour-guides/${uuid}`, {method: "PUT", body: data, auth: true});

export const deleteGuide = (uuid: string) =>
  apiFetch<void>(`/tour-guides/${uuid}`, {method: "DELETE", auth: true});
