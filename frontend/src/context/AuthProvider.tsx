import {useMemo, useState} from "react";
import type {ReactNode} from "react";
import {jwtDecode} from "jwt-decode";
import {AuthContext} from "@/context/AuthContext.ts";
import type {AuthUser} from "@/context/AuthContext.ts";
import {deleteCookie, getCookie, setCookie} from "@/utils/cookies.ts";
import {login} from "@/api/auth.ts";
import type {LoginFields} from "@/schemas/auth.ts";
import type {Role} from "@/types.ts";

// Τι περιέχει το JWT που φτιάχνει το backend (JwtService)
type JwtPayload = {
  sub: string;   // username
  role: Role;
  exp: number;   // λήξη, σε δευτερόλεπτα
}

function readUserFromToken(token: string | null): AuthUser | null {
  if (!token) return null;
  try {
    const payload = jwtDecode<JwtPayload>(token);
    if (payload.exp * 1000 < Date.now()) return null; // έληξε
    return {username: payload.sub, role: payload.role};
  } catch {
    return null; // μη έγκυρο token
  }
}

export const AuthProvider = ({children}: { children: ReactNode }) => {
  // Lazy initial state: το cookie διαβάζεται ΜΙΑ φορά, όταν φορτώνει η εφαρμογή (refresh σελίδας)
  const [token, setToken] = useState<string | null>(() => getCookie("access_token") ?? null);

  const user = useMemo(() => readUserFromToken(token), [token]);

  const loginUser = async (fields: LoginFields) => {
    const res = await login(fields);
    setCookie("access_token", res.token, {expires: 1, sameSite: "lax", path: "/"});
    setToken(res.token);
    return readUserFromToken(res.token);
  }

  const logoutUser = () => {
    deleteCookie("access_token", {path: "/"});
    setToken(null);
  }

  return (
    <AuthContext.Provider value={{user, isAuthenticated: user !== null, loginUser, logoutUser}}>
      {children}
    </AuthContext.Provider>
  )
}
