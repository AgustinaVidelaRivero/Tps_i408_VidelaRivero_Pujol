module Truck ( Truck, newT, freeCellsT, loadT, unloadT, netT )
  where

import Palet
import Stack
import Route

data Truck = Tru [ Stack ] Route deriving (Eq, Show)

newT :: Int -> Int -> Route -> Truck  -- construye un camion según una cantidad de bahias, la altura de las mismas y una ruta
newT cant_bahias altura_bahias (Rou ciudades) = Tru (replicate cant_bahias (newS altura_bahias)) (Rou ciudades)

freeCellsT :: Truck -> Int            -- responde la celdas disponibles en el camion
freeCellsT (Tru stacks route) = foldr (\stack acum -> acum + freeCellsS stack) 0 stacks -- a cada stack de la lista le pido las celdas disponibles y sumo con neutro 0

loadT :: Truck -> Palet -> Truck        -- Carga un palet en el camion
-- Recorremos cada bahia hasta encontrar una en la que entre el palet segun el peso, ciudad de destino y espacio disponible
loadT (Tru stacks route) palet = 
  case loadPalet stacks of
    Just newStacks -> Tru newStacks route     -- Caso sí se encontró una bahía apropiada para el nuevo palet; se construye un nuevo camión con las bahías actualizadas
    Nothing -> Tru stacks route  -- Devuelve el camión original sin cambios
  where
    loadPalet [] = Nothing      -- Caso base: si la lista de pilas esta vacía entonces no se encontró una bahía apropiada para el palet
    loadPalet (stack:rest) | holdsS stack palet route =   -- Si se puede apilar el palet segun la ruta destino,
                             let newStack = stackS stack palet   -- Entonces intenta apilar según el espacio disponible en la bahia y el peso y luego se asigna la bahía modificada a newStack
                             in Just (newStack : rest) -- Devuelve Just con una lista renovada de pilas donde newStack es a la que se añadió el palet, y se mantiene rest igual.
                           | otherwise = fmap (stack :) (loadPalet rest)  -- Recursivamente intenta cargar el palet en el resto de las bahías. fmap añade la bahía actual no modificada (stack) al resultado de la siguiente llamada recursiva, si es exitosa.

unloadT :: Truck -> String -> Truck   -- responde un camion al que se le han descargado los paletes que podían descargarse en la ciudad
unloadT (Tru stacks route) ciudad = Tru (map (`popS` ciudad) stacks) route

netT :: Truck -> Int                  -- responde el peso neto en toneladas de los paletes en el camion
netT (Tru stacks route) = foldr (\stack acum -> acum + netS stack) 0 stacks
