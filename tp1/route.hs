module Route ( Route, newR, inOrderR, inRouteR )
    where

data Route = Rou [ String ] deriving (Eq, Show)

-- newR :: [ String ] -> Route                    -- construye una ruta segun una lista de ciudades
-- newR ciudades = Rou ciudades

-- POSIBLE MEJORA: no permitir la creacion de rutas vacias
newR :: [String] -> Route
newR ciudades
    | null ciudades = error "Error: La ruta no puede estar vacía"
    | otherwise = Rou ciudades


-- inOrderR :: Route -> String -> String -> Bool  -- indica si la primer ciudad consultada esta antes que la segunda ciudad en la ruta
-- inOrderR (Rou ciudades) ciudad1 ciudad2 | null ciudades = error "No hay ciudades en la ruta"
--                                         | not (inRouteR (Rou ciudades) ciudad1) || not (inRouteR (Rou ciudades) ciudad2) = error "Al menos una de las ciudades no está en la ruta"
--                                         | head ciudades == ciudad1 = True
--                                         | head ciudades == ciudad2 = False
--                                         | otherwise = inOrderR (Rou (tail ciudades)) ciudad1 ciudad2

-- POSIBLE MEJORA: que el programa no termine si no encuentra la ciudad
inOrderR :: Route -> String -> String -> Bool
inOrderR (Rou ciudades) ciudad1 ciudad2
    | not (inRouteR (Rou ciudades) ciudad1) || not (inRouteR (Rou ciudades) ciudad2) = False
    | otherwise = case dropWhile (/= ciudad1) ciudades of
                    [] -> False
                    (_:cs) -> ciudad2 `elem` cs


-- inRouteR :: Route -> String -> Bool -- indica si la ciudad consultada está en la ruta
-- inRouteR (Rou ciudades) ciudad | null ciudades = False
--                                 | head ciudades == ciudad = True
--                                 | otherwise = inRouteR (Rou (tail ciudades)) ciudad

inRouteR :: Route -> String -> Bool -- indica si la ciudad consultada está en la ruta
inRouteR (Rou ciudades) ciudad = ciudad `elem` ciudades -- es mas eficiente que la implementacion recursiva
