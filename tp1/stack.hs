module Stack ( Stack, newS, freeCellsS, stackS, netS, holdsS, popS )
  where

import Palet (Palet, newP, destinationP, netP)  -- Sin los constructores
import Route (Route, newR, inOrderR, inRouteR) -- Sin los constructores

data Stack = Sta [ Palet ] Int deriving (Eq, Show)

newS :: Int -> Stack                      -- construye una bahia con la capacidad indicada (la altura)
newS cap_max = Sta [] cap_max

freeCellsS :: Stack -> Int                -- responde la celdas disponibles en la bahia
freeCellsS (Sta palets cap_max) = cap_max - length palets

stackS :: Stack -> Palet -> Stack         -- apila el palet indicado en la bahia
-- para apilar un palet en la bahia me fijo que tenga espacio disponible 
-- que no supere las 10 toneladas
stackS (Sta palets cap_max) palet | freeCellsS (Sta palets cap_max) == 0 = Sta palets cap_max  -- No hay espacio, no carga
                                  | netS (Sta palets cap_max) + netP palet > 10 = Sta palets cap_max  -- No tolera mas peso, no carga
                                  | otherwise = Sta (palets ++ [palet]) cap_max

netS :: Stack -> Int                      -- responde el peso neto de los paletes en la bahia
netS (Sta palets cap_max) = foldr (\palet acum -> acum + netP palet) 0 palets

holdsS :: Stack -> Palet -> Route -> Bool -- indica si la bahia puede aceptar el palet considerando las ciudades en la ruta
holdsS (Sta palets _) palet route | null palets = True
                                  | inOrderR route (destinationP palet) (destinationP (last palets)) = True
                                  | otherwise = False

popS :: Stack -> String -> Stack          -- quita del tope los paletes con destino en la ciudad indicada
popS (Sta palets cap_max) ciudad | null palets = Sta palets cap_max
                                 | destinationP (last palets) == ciudad = popS (Sta (init palets) cap_max) ciudad -- si el destino coincide
                                 | otherwise = Sta palets cap_max -- si el destino no coincide queda igual