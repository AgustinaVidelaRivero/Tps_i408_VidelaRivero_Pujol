module Stack ( Stack, newS, freeCellsS, stackS, netS, holdsS, popS )
  where

import Palet
import Route

data Stack = Sta [ Palet ] Int deriving (Eq, Show)

newS :: Int -> Stack                      -- construye una Pila con la capacidad indicada (la altura)
newS cap_max = Sta [] cap_max

freeCellsS :: Stack -> Int                -- responde la celdas disponibles en la pila
freeCellsS (Sta palets cap_max) = cap_max - length palets

stackS :: Stack -> Palet -> Stack         -- apila el palet indicado en la pila
-- para apilar un palet en la pila me fijo que tenga espacio disponible 
-- que no supere las 10 toneladas
stackS (Sta palets cap_max) palet | freeCellsS (Sta palets cap_max) == 0 = error "No hay espacio disponible para agregar otro palet"
                                  | netS (Sta palets cap_max) + netP palet > 10 = error "El peso de la bahía supera las 10 toneladas"
                                  | otherwise = Sta (palets ++ [palet]) cap_max

netS :: Stack -> Int                      -- responde el peso neto de los paletes en la pila
netS (Sta palets cap_max) = foldr (\palet acum -> acum + netP palet) 0 palets

holdsS :: Stack -> Palet -> Route -> Bool -- indica si la pila puede aceptar el palet considerando las ciudades en la ruta
holdsS (Sta palets cap_max) (Pal ciudad) (Rou ciudades) | null palets = True 
                                                        | inOrder ciudad (destinationP (last palets)) ciudades = True 
                                                        | otherwise = False

popS :: Stack -> String -> Stack          -- quita del tope los paletes con destino en la ciudad indicada
popS (Sta palets cap_max) ciudad | null palets = Sta palets cap_max
                                 | destinationP (last palets) == ciudad = popS (Sta (init palets) cap_max) ciudad -- si el destino coincide
                                 | otherwise = Sta palets cap_max -- si el destino no coincide queda igual