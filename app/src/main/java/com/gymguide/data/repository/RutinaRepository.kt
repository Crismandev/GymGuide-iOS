
package com.gymguide.data.repository

import com.gymguide.data.local.dao.RutinaDao
import com.gymguide.data.local.entity.Rutina
import kotlinx.coroutines.flow.Flow

class RutinaRepository(
    private val rutinaDao: RutinaDao
) {

    fun observarRutinas(
        filtro: String = ""
    ): Flow<List<Rutina>> {
        return rutinaDao.observarResultinas(filtro)
    }

    suspend fun obtenerRutinaActiva(): Rutina? {
        return rutinaDao.getRutinaActiva()
    }

    suspend fun activarRutina(idRutina: Int) {
        rutinaDao.activarRutina(idRutina)
    }

    suspend fun eliminarRutina(rutina: Rutina) {
        rutinaDao.eliminarRutina(rutina)
    }
}
