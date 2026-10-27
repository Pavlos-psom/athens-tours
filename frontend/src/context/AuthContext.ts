import {createContext} from "react";
import type {LoginFields} from "@/schemas/auth.ts";
import type {Role} from "@/types.ts";

export type AuthUser = {
  username: string;
  role: Role;
}

export type AuthContextProps = {
  user: AuthUser | null;
  isAuthenticated: boolean;
  loginUser: (fields: LoginFields) => Promise<AuthUser | null>;
  logoutUser: () => void;
}

export const AuthContext = createContext<AuthContextProps | undefined>(undefined);
