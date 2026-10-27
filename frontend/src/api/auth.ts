import {apiFetch} from "@/api/client.ts";
import type {LoginFields, RegisterFields} from "@/schemas/auth.ts";

export type LoginResponse = { token: string };
export type RegisterResponse = { uuid: string; username: string; role: string };

export function login({username, password}: LoginFields) {
  return apiFetch<LoginResponse>("/auth/authenticate", {method: "POST", body: {username, password}});
}

export function register({username, password}: RegisterFields) {
  return apiFetch<RegisterResponse>("/auth/register", {method: "POST", body: {username, password}});
}
