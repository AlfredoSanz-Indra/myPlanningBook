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
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.QuerySnapshot
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
                    return@async vehicleResp
                }
                result = defer.await()
            } //scope
        }
        catch(err: Exception) {
            Klog.line("Error: $err")
        }

        Klog.linedbg("VehicleRepositoryImpl", "insertVehicle", "result: $result")
        return result
    }

    override suspend fun getVehicles(userEmail: String): SimpleDataVehicleResponse {
        Klog.line("VehicleRepositoryImpl", "getVehicles", "userEmail: ${userEmail}")
        var result = SimpleDataVehicleResponse(false, 100, "fail getting vehicles")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<QuerySnapshot?> =
                    FirebaseSession.db.collection(Collections.VEHICLES)
                        .whereEqualTo(Documents.VEHICLE_USEREMAIL, userEmail)
                        .orderBy(Documents.VEHICLE_ADQUISITION_DATE, Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener {
                            Klog.line("VehicleRepositoryImpl", "getVehicles", "addOnSuccessListener -> it: ${it.documents.size}")
                        }
                        .addOnFailureListener {
                            e -> Klog.line("VehicleRepositoryImpl", "getVehicles","addOnFailureListener -> ERR -> $e")
                        }

                task.await()

                var vehicleResp: SimpleDataVehicleResponse
                if (task.isSuccessful) {
                    Klog.line("VehicleRepositoryImpl", "getVehicles", "task is successful")
                    var vehicleList: MutableList<Vehicle> = mutableListOf()
                    for (document in task.result?.documents!!) {
                        var vehicleFound: Vehicle = Vehicle(
                            document.id,
                            document.get(Documents.VEHICLE_NAME) as String,
                            document.get(Documents.VEHICLE_MODEL) as String,
                            document.get(Documents.VEHICLE_NOTES) as String?,
                            document.get(Documents.VEHICLE_ADQUISITION_DATE) as Long,
                            (document.get(Documents.VEHICLE_ADQUISITION_YEAR) as Long).toInt(),
                            (document.get(Documents.VEHICLE_ADQUISITION_MONTH) as Long).toInt(),
                            (document.get(Documents.VEHICLE_ADQUISITION_DAY) as Long).toInt()
                        )
                        vehicleList.add(vehicleFound)
                    }
                    vehicleResp = SimpleDataVehicleResponse(true, 200, "Vehicles obtained - ${task.result?.documents?.size}")
                    vehicleResp.vehicleList = vehicleList

                } else {
                    Klog.line("VehicleRepositoryImpl", "getVehicles", "error cause: ${task.exception?.cause}")
                    Klog.line("VehicleRepositoryImpl", "getVehicles", "error message: ${task.exception?.message}")

                    vehicleResp = SimpleDataVehicleResponse(false, 400, "Getting Vehicles failed.")
                }
                return@async vehicleResp
            }
            result = defer.await()
        } //scope

        Klog.line("VehicleRepositoryImpl", "getVehicles", "result: $result")
        return result
    }
}