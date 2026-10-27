import {getCookie} from "@/utils/cookies.ts";

export const API_URL: string = import.meta.env.VITE_API_URL ?? "http://localhost:8080/api/v1";

// Το σφάλμα που πετάμε όταν το backend απαντήσει με status που ΔΕΝ είναι 2xx.
// Κρατάει όσα μας χρειάζονται για να δείξουμε σωστό μήνυμα.
export class ApiError extends Error {
  status: number;
  code?: string;                         // π.χ. "ScheduleFull", "BAD_CREDENTIALS"
  fieldErrors?: Record<string, string>;  // για validation 400: { price: "Price must be..." }

  constructor(message: string, status: number, code?: string, fieldErrors?: Record<string, string>) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.code = code;
    this.fieldErrors = fieldErrors;
  }
}

type RequestOptions = {
  method?: "GET" | "POST" | "PUT" | "DELETE";
  body?: unknown;                        // θα γίνει JSON.stringify
  auth?: boolean;                        // true => στέλνουμε Authorization: Bearer <token>
};

// Η ΜΙΑ συνάρτηση από την οποία περνάνε ΟΛΑ τα requests.
// <T> = ο τύπος των δεδομένων που περιμένουμε πίσω (generic).
export async function apiFetch<T>(path: string, {method = "GET", body, auth = false}: RequestOptions = {}): Promise<T> {
  const headers: Record<string, string> = {};

  if (body !== undefined) {
    headers["Content-Type"] = "application/json";
  }
  if (auth) {
    const token = getCookie("access_token");
    if (token) headers["Authorization"] = `Bearer ${token}`;
  }

  let res: Response;
  try {
    res = await fetch(API_URL + path, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    });
  } catch {
    // Το fetch πετάει (reject) μόνο όταν δεν φτάσει καν στον server (δίκτυο, CORS, server κλειστός)
    throw new ApiError("Cannot reach the server. Is the backend running?", 0);
  }

  if (!res.ok) {
    throw await buildApiError(res);
  }

  // 204 No Content (π.χ. DELETE): δεν υπάρχει body να διαβάσουμε
  if (res.status === 204) {
    return undefined as T;
  }
  return (await res.json()) as T;
}

async function buildApiError(res: Response): Promise<ApiError> {
  let data: unknown = null;
  try {
    data = await res.json();
  } catch {
    // άδειο ή μη-JSON body
  }

  if (data && typeof data === "object") {
    const obj = data as Record<string, unknown>;

    // Σφάλμα επιχειρησιακής λογικής / security: { code, description }
    if (typeof obj.code === "string" && typeof obj.description === "string") {
      return new ApiError(obj.description, res.status, obj.code);
    }

    // Validation (400): { fieldName: "message", ... }
    if (res.status === 400) {
      const fieldErrors: Record<string, string> = {};
      for (const [key, value] of Object.entries(obj)) {
        if (typeof value === "string") fieldErrors[key] = value;
      }
      const message = Object.values(fieldErrors).join(" · ") || "Invalid request";
      return new ApiError(message, 400, "VALIDATION", fieldErrors);
    }
  }

  return new ApiError(res.statusText || "Unexpected error", res.status);
}
