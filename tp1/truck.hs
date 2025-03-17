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

loadT :: Truck -> Palet -> Truck      -- carga un palet en el camion
loadT (Tru stacks route) palet | freeCellsT (Tru stacks route) == 0 = error "No hay espacio disponible"
                            -- me falta seguir

unloadT :: Truck -> String -> Truck   -- responde un camion al que se le han descargado los paletes que podían descargarse en la ciudad

netT :: Truck -> Int                  -- responde el peso neto en toneladas de los paletes en el camion
netT (Tru stacks route) = foldr (\stack acum -> acum + netS stack) 0 stacks
