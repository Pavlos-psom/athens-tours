import {apiFetch} from "@/api/client.ts";
import type {TourSchedule} from "@/types.ts";

export type ScheduleCreatePayload = {
  tourUuid: string;
  tourGuideUuid: string;
  startsAt: string;
  capacity: number;
};
export type ScheduleUpdatePayload = Omit<ScheduleCreatePayload, "tourUuid">;

export const getSchedules = (tourUuid?: string) =>
  apiFetch<TourSchedule[]>(tourUuid ? `/tour-schedules?tourUuid=${tourUuid}` : "/tour-schedules");

export const getSchedule = (uuid: string) => apiFetch<TourSchedule>(`/tour-schedules/${uuid}`);

// Μόνο για GUIDE
export const getMySchedule = () =>
  apiFetch<TourSchedule[]>("/tour-schedules/my-schedule", {auth: true});

export const createSchedule = (data: ScheduleCreatePayload) =>
  apiFetch<TourSchedule>("/tour-schedules", {method: "POST", body: data, auth: true});

export const updateSchedule = (uuid: string, data: ScheduleUpdatePayload) =>
  apiFetch<TourSchedule>(`/tour-schedules/${uuid}`, {method: "PUT", body: data, auth: true});

export const deleteSchedule = (uuid: string) =>
  apiFetch<void>(`/tour-schedules/${uuid}`, {method: "DELETE", auth: true});
