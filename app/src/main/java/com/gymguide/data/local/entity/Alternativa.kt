package com.gymguide.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Tabla alternativa: relación N:M ejercicio-ejercicio (RF04, RN04).
 * Clave primaria COMPUESTA: el par (original, alternativo) no se repite.
 * Dos FK a la MISMA tabla: el ejercicio ocupado y su reemplazo.
 * La regla "ids diferentes" se valida en el Repository (no en Room).
 */
@Entity(
    tableName = "alternativa",
    primaryKeys = ["id_ejercicio", "id_alternativo"],
    foreignKeys = [
        ForeignKey(
            entity = Ejercicio::class,
            parentColumns = ["id_ejercicio"],
            childColumns = ["id_ejercicio"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Ejercicio::class,
            parentColumns = ["id_ejercicio"],
            childColumns = ["id_alternativo"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Alternativa(
    @ColumnInfo(name = "id_ejercicio") val idEjercicio: Int,
    @ColumnInfo(name = "id_alternativo") val idAlternativo: Int,
    @ColumnInfo(name = "motivo") val motivo: String? = null
)