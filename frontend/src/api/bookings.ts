import {apiFetch} from "@/api/client.ts";
import type {Booking} from "@/types.ts";

// CUSTOMER
export const createBooking = (scheduleUuid: string) =>
  apiFetch<Booking>("/bookings", {method: "POST", body: {scheduleUuid}, auth: true});

export const getMyBookings = () => apiFetch<Booking[]>("/bookings/my-bookings", {auth: true});

export const cancelBooking = (uuid: string) =>
  apiFetch<void>(`/bookings/${uuid}`, {method: "DELETE", auth: true});

// ADMIN
export const getAllBookings = () => apiFetch<Booking[]>("/bookings", {auth: true});
