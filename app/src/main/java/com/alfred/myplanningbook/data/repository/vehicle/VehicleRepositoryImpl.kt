package com.alfred.myplanningbook.data.repository.vehicle

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
import com.alfred.myplanningbook.data.model.Documents
import com.alfred.myplanningbook.data.model.SimpleDataVehicleResponse
import com.alfred.myplanningbook.domain.model.vehicle.Vehicle
import com.alfred.myplanningbook.domain.repositoryapi.vehicle.VehicleRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * @author Alfredo Sanz
 * @time 2025
 */
class VehicleRepositoryImpl(private val ioDispatcher: CoroutineDispatcher): VehicleRepository {

    override suspend fun insertVehicle(entity: Vehicle, userEmail: String): SimpleDataVehicleResponse {
        Klog.line("VehicleRepositoryImpl", "insertVehicle", "name: ${entity.name}")
        var result = SimpleDataVehicleResponse(false, 100, "fail inserting")

        val vehicleData = hashMapOf(
            Documents.VEHICLE_USEREMAIL to userEmail,
            Documents.VEHICLE_NAME to entity.name,
            Documents.VEHICLE_MODEL to entity.model,
            Documents.VEHICLE_NOTES to entity.notes,
            Documents.VEHICLE_ADQUISITION_DATE to entity.dateInMillis,
            Documents.VEHICLE_ADQUISITION_YEAR to entity.year,
            Documents.VEHICLE_ADQUISITION_MONTH to entity.month,
            Documents.VEHICLE_ADQUISITION_DAY to entity.day
        )
        try {
            withContext(ioDispatcher) {
                val defer = async(ioDispatcher) {
                    val task: Task<DocumentReference?> = FirebaseSession.db.collection(Collections.VEHICLES)
                            .add(vehicleData)
                            .addOnSuccessListener { Klog.line("VehicleRepositoryImpl", "insertVehicle", "addOnSuccessListener -> it: ${it.id}")}
                            .addOnFailureListener { e -> Klog.line("VehicleRepositoryImpl", "insertVehicle", "addOnFailureListener -> ERR -> $e") }

                    task.await()
                    var vehicleResp: SimpleDataVehicleResponse
                    if(task.isSuccessful) {
                        Klog.line("VehicleRepositoryImpl", "insertVehicle", "task is successful")
                        vehicleResp = SimpleDataVehicleResponse(true, 200, "inserted vehicle")
                        entity.id = task.result?.id
                        vehicleResp.vehicle = entity
                    }
                    else {
                        Klog.line("VehicleRepositoryImpl", "insertVehicle", "error cause: ${task.exception?.cause}")
                        Klog.line("VehicleRepositoryImpl", "insertVehicle", "error message: ${task.exception?.message}")

                        vehicleResp = SimpleDataVehicleResponse(false, 400, "Inserting Vehicle failed.")
                    }
                    Klog.line("VehicleRepositoryImpl", "insertVehicle", "Before return")
                    return@async vehicleResp
                }

                Klog.line("VehicleRepositoryImpl", "insertVehicle", "Before final await")
                result = defer.await()
            } //scope
        } catch(err: Exception) {
            Klog.line("Error: $err")
        }

        Klog.linedbg("VehicleRepositoryImpl", "insertVehicle", "result: $result")
        return result
    }
}