package com.gymguide.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.gymguide.data.local.entity.DetalleRutina
import com.gymguide.data.local.entity.DiaRutina
import com.gymguide.data.local.entity.Rutina
import kotlinx.coroutines.flow.Flow

/**
 * DAO del CRUD de rutinas: los 4 puntos de "Manejo de SQLite" de la rúbrica.
 * Cubre RF05, RF06, RN05 (una activa) y RN06 (borrado en cascada).
 */
@Dao
interface RutinaDao {

    /** Lista observable con búsqueda: la pantalla se actualiza sola (RF05). */
    @Query(
        "SELECT * FROM rutina " +
                "WHERE nombre LIKE '%' || :filtro || '%' " +
                "ORDER BY fecha_creacion DESC"
    )
    fun observarResultinas(filtro: String): Flow<List<Rutina>>

    @Query("SELECT * FROM rutina WHERE activa = 1 LIMIT 1")
    suspend fun getRutinaActiva(): Rutina?

    /** Carga la rutina completa para el formulario en modo edición. */
    @Transaction
    @Query("SELECT * FROM rutina WHERE id_rutina = :idRutina")
    suspend fun getRutinaCompleta(idRutina: Int): RutinaCompleta?

    // ---- Operaciones simples ----

    @Insert
    suspend fun insertRutina(rutina: Rutina): Long

    @Update
    suspend fun updateRutina(rutina: Rutina)

    @Delete
    suspend fun deleteRutina(rutina: Rutina)

    @Insert
    suspend fun insertDia(dia: DiaRutina): Long

    @Insert
    suspend fun insertDetalle(detalle: DetalleRutina)

    @Query("DELETE FROM dia_rutina WHERE id_rutina = :idRutina")
    suspend fun deleteDiasDeRutina(idRutina: Int)

    // ---- TRANSACCIONES (reglas de negocio hechas código) ----

    /** CREATE completo: rutina + días + detalles en una sola transacción. */
    @Transaction
    suspend fun guardarRutinaCompleta(rutina: Rutina, dias: List<DiaFormulario>): Long {
        val idRutina = insertRutina(rutina).toInt()
        insertDiasConDetalles(idRutina, dias)
        return idRutina.toLong()
    }

    /** UPDATE completo: reemplaza días y detalles conservando el id. */
    @Transaction
    suspend fun editarRutinaCompleta(rutina: Rutina, dias: List<DiaFormulario>) {
        updateRutina(rutina)
        deleteDiasDeRutina(rutina.idRutina) // la cascada borra los detalles (RN06)
        insertDiasConDetalles(rutina.idRutina, dias)
    }

    /** DELETE: la cascada de las FK borra días y detalles solos (RN06). */
    @Transaction
    suspend fun eliminarRutina(rutina: Rutina) {
        deleteRutina(rutina)
    }

    /** RN05: desactiva todas y activa solo la elegida, atómico. */
    @Transaction
    suspend fun activarRutina(idRutina: Int) {
        desactivarTodas()
        activar(idRutina)
    }

    /** Uso interno de las transacciones: no llamar desde ViewModels. */
    @Transaction
    suspend fun insertDiasConDetalles(idRutina: Int, dias: List<DiaFormulario>) {
        for ((indice, dia) in dias.withIndex()) {
            val idDia = insertDia(
                dia.detalles.firstOrNull()?.let { _ ->
                    DiaRutina(
                        idRutina = idRutina,
                        numeroDia = indice + 1,
                        nombreDia = dia.nombreDia
                    )
                } ?: DiaRutina(
                    idRutina = idRutina,
                    numeroDia = indice + 1,
                    nombreDia = dia.nombreDia
                )
            ).toInt()
            for (detalle in dia.detalles) {
                insertDetalle(detalle.copy(idDia = idDia))
            }
        }
    }

    @Query("UPDATE rutina SET activa = 0")
    suspend fun desactivarTodas()

    @Query("UPDATE rutina SET activa = 1 WHERE id_rutina = :idRutina")
    suspend fun activar(idRutina: Int)
}