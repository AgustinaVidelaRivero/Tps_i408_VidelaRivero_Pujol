module Stack ( Stack, newS, freeCellsS, stackS, netS, holdsS, popS )
  where

import Palet (Palet, newP, destinationP, netP)  -- Sin los constructores
import Route (Route, newR, inOrderR, inRouteR) -- Sin los constructores

data Stack = Sta [ Palet ] Int deriving (Eq, Show)

newS :: Int -> Stack -- construye una Pila con la capacidad indicada
newS cap_max 
    | cap_max <= 0 = error "Error: La altura de la pila debe ser mayor a 0"
    | otherwise = Sta [] cap_max


freeCellsS :: Stack -> Int -- responde la celdas disponibles en la pila
freeCellsS (Sta palets cap_max) = cap_max - length palets

stackS :: Stack -> Palet -> Stack -- apila el palet indicado en la pila
stackS (Sta palets cap_max) palet | freeCellsS (Sta palets cap_max) == 0 = Sta palets cap_max  -- No hay espacio, no carga
                                  | netS (Sta palets cap_max) + netP palet > 10 = Sta palets cap_max  -- No tolera mas peso, no carga
                                  | otherwise = Sta (palets ++ [palet]) cap_max


netS :: Stack -> Int -- responde el peso neto de los paletes en la pila
netS (Sta palets cap_max) = foldr (\palet acum -> acum + netP palet) 0 palets

holdsS :: Stack -> Palet -> Route -> Bool -- indica si la pila puede aceptar el palet considerando las ciudades en la ruta
holdsS (Sta palets _) palet route | null palets = True
                                  | (destinationP palet == destinationP (head (reverse palets))) = True   -- si la ciudad destino del ultimo palet es igual a la del palet que quiero apilar
                                  | inOrderR route (destinationP palet) (destinationP (head(reverse palets))) = True
                                  | otherwise = False

popS :: Stack -> String -> Stack -- quita del tope los paletes con destino en la ciudad indicada
popS (Sta palets cap_max) ciudad | null palets = Sta palets cap_max
                                 | destinationP (last palets) == ciudad = popS (Sta (init palets) cap_max) ciudad -- si el destino coincide
                                 | otherwise = Sta palets cap_max -- si el destino no coincide queda igual