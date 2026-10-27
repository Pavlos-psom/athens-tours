export function formatDateTime(iso: string): string {
  return new Date(iso).toLocaleString("en-GB", {dateStyle: "medium", timeStyle: "short"});
}

export function formatPrice(value: number): string {
  return new Intl.NumberFormat("en-GB", {style: "currency", currency: "EUR"}).format(value);
}

export function formatDuration(minutes: number): string {
  const h = Math.floor(minutes / 60);
  const m = minutes % 60;
  if (h === 0) return `${m} min`;
  if (m === 0) return `${h} h`;
  return `${h} h ${m} min`;
}

// "English, Greek ,  " -> ["English", "Greek"]
export function parseLanguages(text: string): string[] {
  return text
    .split(",")
    .map((l) => l.trim())
    .filter((l) => l.length > 0);
}

// Το <input type="datetime-local"> θέλει "YYYY-MM-DDTHH:mm"
export function toDateTimeLocalValue(iso: string): string {
  return iso.slice(0, 16);
}

// Το backend (LocalDateTime) θέλει "YYYY-MM-DDTHH:mm:ss"
export function toBackendDateTime(value: string): string {
  return value.length === 16 ? `${value}:00` : value;
}
