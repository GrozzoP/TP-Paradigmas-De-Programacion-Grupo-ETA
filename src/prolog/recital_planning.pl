:- use_module(library(lists)).

% min_trainings(+RolesFaltantes, -MinEntrenamientos)
% RolesFaltantes: lista de átomos, uno por rol faltante (puede haber repetidos)
% MinEntrenamientos: cantidad minima de entrenamientos necesarios.

min_trainings(RolesFaltantes, MinEntrenamientos) :-
    length(RolesFaltantes, MinEntrenamientos).
