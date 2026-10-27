// Τύποι που αντιστοιχούν 1-1 στα DTOs του Spring Boot backend.
// (Οι τύποι υπάρχουν μόνο στο TypeScript – σβήνονται όταν γίνεται το build.)

export type Role = "ADMIN" | "GUIDE" | "CUSTOMER";

export type Category = {
  id: number;
  name: string;
  description: string | null;
};

export type Tour = {
  uuid: string;
  name: string;
  description: string | null;
  price: number;
  durationMinutes: number;
  category: Category;
};

export type TourGuide = {
  uuid: string;
  username: string;
  phone: string | null;
  bio: string | null;
  languages: string[];
};

export type TourSchedule = {
  uuid: string;
  tour: Tour;
  tourGuide: TourGuide;
  startsAt: string; // LocalDateTime από το backend, π.χ. "2026-11-01T10:00:00"
  capacity: number;
};

export type Booking = {
  uuid: string;
  schedule: TourSchedule;
  customerUsername: string;
  bookedAt: string; // Instant, π.χ. "2026-10-10T12:30:00Z"
};
