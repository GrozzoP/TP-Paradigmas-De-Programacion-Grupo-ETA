:- use_module(library(lists)).

% min_trainings(+RolesFaltantes, -MinEntrenamientos)
% RolesFaltantes: lista de átomos, uno por rol faltante (puede haber repetidos)
% MinEntrenamientos: cantidad minima de entrenamientos necesarios.

min_trainings(RolesFaltantes, MinEntrenamientos) :-
    length(RolesFaltantes, MinEntrenamientos).
    
% min_trainings_with_cost(+RolesFaltantes, +CostoBase, -MinEntrenamientos, -CostoTotal)
% RolesFaltantes: misma lista de roles que antes
% CostoBase: costo de UN entrenamiento (mismo valor para todos)
% MinEntrenamientos: cantidad minima de entrenamientos (sale de min_trainings/2)
% CostoTotal: MinEntrenamientos * CostoBase
min_trainings_with_cost(RolesFaltantes, CostoBase, MinEntrenamientos, CostoTotal) :-
    min_trainings(RolesFaltantes, MinEntrenamientos),
    CostoTotal is MinEntrenamientos * CostoBase.
