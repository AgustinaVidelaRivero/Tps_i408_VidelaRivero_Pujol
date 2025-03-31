module Truck ( Truck, newT, freeCellsT, loadT, unloadT, netT )
  where

import Palet  
import Stack (Stack, newS, freeCellsS, stackS, popS, netS, holdsS)
import Route (Route, newR, inOrderR, inRouteR)

data Truck = Tru [ Stack ] Route deriving (Eq, Show)

newT :: Int -> Int -> Route -> Truck -- construye un camion según una cantidad de bahias, la altura de las mismas y una ruta
newT cant_bahias altura_bahias ruta
    | cant_bahias <= 0 = error "Error: El camión debe tener al menos una bahía"
    | altura_bahias <= 0 = error "Error: La altura de las bahías debe ser mayor a 0"
    | otherwise = Tru (replicate cant_bahias (newS altura_bahias)) ruta


freeCellsT :: Truck -> Int            -- responde la celdas disponibles en el camion
freeCellsT (Tru stacks route) = foldr (\stack acum -> acum + freeCellsS stack) 0 stacks 

loadT :: Truck -> Palet -> Truck        -- Carga un palet en el camion
-- Recorremos cada bahia hasta encontrar una en la que entre el palet segun el peso, ciudad de destino y espacio disponible
loadT (Tru stacks route) palet = 
  case loadPalet stacks of
    Just newStacks -> Tru newStacks route     -- Caso sí se encontró una bahía apropiada para el nuevo palet
    Nothing -> Tru stacks route  
  where
    loadPalet [] = Nothing    
    loadPalet (stack:rest) | holdsS stack palet route =   -- Si se puede apilar el palet segun la ruta destino,
                             let newStack = stackS stack palet   -- intenta apilar según el espacio disponible en la bahia y el peso y luego se asigna la bahía modificada a newStack
                             in Just (newStack : rest) 
                           | otherwise = fmap (stack :) (loadPalet rest)  -- Recursivamente intenta cargar el palet en el resto de las bahías. 

unloadT :: Truck -> String -> Truck   -- responde un camion al que se le han descargado los paletes que podían descargarse en la ciudad
unloadT (Tru stacks route) ciudad = Tru (map (`popS` ciudad) stacks) route

netT :: Truck -> Int                  -- responde el peso neto en toneladas de los paletes en el camion
netT (Tru stacks route) = foldr (\stack acum -> acum + netS stack) 0 stacks
