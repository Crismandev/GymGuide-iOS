package com.gymguide.data.local.dao

import androidx.room.Embedded
import androidx.room.Relation
import com.gymguide.data.local.entity.DetalleRutina
import com.gymguide.data.local.entity.Ejercicio
import com.gymguide.data.local.entity.Rutina

/**
 * Modelos auxiliares para consultas con JOIN y relaciones anidadas.
 * NO son tablas: son "formas" que Room arma al leer datos combinados.
 */

/** Detalle de ejercicio + nombre de su grupo muscular (pantalla Detalle). */
data class EjercicioConGrupo(
    @Embedded val ejercicio: Ejercicio,
    val nombreGrupo: String
)

/** Fila de la lista de alternativas (pantalla Alternativas, RF04). */
data class AlternativaView(
    val idEjercicio: Int,
    val nombre: String,
    val equipo: String,
    val nivel: String,
    val imagen: String?,
    val motivo: String?
)

/** Un ejercicio dentro de un día de rutina, con su data class completa. */
data class DetalleConEjercicio(
    @Embedded val detalle: DetalleRutina,
    @Relation(
        parentColumn = "id_ejercicio",
        entityColumn = "id_ejercicio"
    )
    val ejercicio: Ejercicio
)

/** Un día de rutina con todos sus ejercicios (vista y edición). */
data class DiaConDetalles(
    @Embedded val dia: com.gymguide.data.local.entity.DiaRutina,
    @Relation(
        parentColumn = "id_dia",
        entityColumn = "id_dia"
    )
    val detalles: List<DetalleConEjercicio>
)

/** Rutina completa: cabecera + días + ejercicios (cargar formulario en edición). */
data class RutinaCompleta(
    @Embedded val rutina: Rutina,
    @Relation(
        parentColumn = "id_rutina",
        entityColumn = "id_rutina"
    )
    val dias: List<DiaConDetalles>
)

/**
 * ENTRADA del formulario: un día con su nombre y sus ejercicios.
 * Los idDia de los detalles van en 0 como placeholder;
 * el DAO los reemplaza por el id real al insertar.
 */
data class DiaFormulario(
    val nombreDia: String,
    val detalles: List<DetalleRutina>
)