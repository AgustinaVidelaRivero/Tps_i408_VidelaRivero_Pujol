module Palet ( Palet, newP, destinationP, netP )
  where

data Palet = Pal String Int deriving (Eq, Show)

-- No permitimos la creacion de palets con peso 0 o negativo, o con peso mayor a 10 porque no se pueden cargar en ninguna bahia
newP :: String -> Int -> Palet -- construye un Palet dada una ciudad de destino y un peso en toneladas
newP ciudad_destino peso
     | peso <= 0 = error "Error: El peso del palet debe ser mayor a 0"
     | peso > 10 = error "Error: El peso del palet no puede superar las 10 toneladas"
     | otherwise = Pal ciudad_destino peso

destinationP :: Palet -> String  -- responde la ciudad destino del palet
destinationP (Pal ciudad_destino peso) = ciudad_destino

netP :: Palet -> Int             -- responde el peso en toneladas del palet
netP (Pal ciudad_destino peso) = peso