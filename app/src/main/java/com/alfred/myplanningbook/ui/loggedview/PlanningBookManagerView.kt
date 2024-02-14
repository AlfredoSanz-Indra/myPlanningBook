package com.alfred.myplanningbook.ui.loggedview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.ui.common.CommonViewComp
import com.alfred.myplanningbook.ui.loggedview.viewmodel.BookMenuViewModel
import com.alfred.myplanningbook.ui.loggedview.viewmodel.PlanningBookManagerUiState
import com.alfred.myplanningbook.ui.loggedview.viewmodel.PlanningBookManagerViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class PlanningBookManagerView {


    @Composable
    fun createView(onBack: () -> Unit) {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(uiState.currentPlanningBook) {
            viewModel.loadPlanningBooks()
            Klog.line("PlanningBookManagerView", "createView", "currentPlanningBook:  ${uiState.currentPlanningBook}")
        }

        MaterialTheme(colorScheme = MaterialTheme.colorScheme) {
            Column(
                Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                if(uiState.currentPlanningBook.isEmpty()) {
                    Spacer(modifier = Modifier.height(30.dp))
                    loading()
                }
                else {
                    Spacer(modifier = Modifier.height(30.dp))
                    errorGeneralField()
                    headerMessageOptional(uiState)

                    Spacer(modifier = Modifier.height(10.dp))
                    PBHeaderSection(onBack)

                    Spacer(modifier = Modifier.height(10.dp))
                    pbListSection()
                }
            }
        }
    }

    @Composable
    private fun errorGeneralField() {

        val viewModel: BookMenuViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.generalError) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                uiState.generalErrorText,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Red
                )
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    @Composable
    private fun loading() {

        OutlinedButton(modifier = Modifier
            .width(200.dp)
            .height(70.dp),
            colors = CommonViewComp.getActionsButtonColour(),
            onClick = {
            }
        ) {
            Text("Loading State!! Please Wait")
        }
    }

    @Composable
    private fun headerMessageOptional(uiState: PlanningBookManagerUiState) {
        if(uiState.planningBookList.isEmpty()) {
            Text(
                uiState.currentPlanningBook,
                color = Color.Blue,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Red
                )
            )
        }
    }

    @Composable
    private fun PBHeaderSection(onBack: () -> Unit) {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if(uiState.isToCreatePB) {
            pbCreationSection()
        }
        else {
            pbCreationActions(onBack)
        }
    }

    @Composable
    private fun pbCreationActions(onBack: () -> Unit) {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()

        Row {
            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getActionsButtonColour(),
                onClick = {
                    Klog.line("PlanningBookManagerView","headerActions","create Planning Book button clicked")
                    viewModel.showPBCreationSection(true);
                }
            ) {
                Text("Create Planning Book")
            }

            OutlinedButton(modifier = Modifier
                .width(200.dp)
                .height(70.dp),
                colors = CommonViewComp.getSecondaryButtonColour(),
                onClick = {
                    Klog.line("PlanningBookManagerView", "headerActions", "back Button clicked")
                    onBack()
                }
            ) {
                Text("Back")
            }
        }
    }

    @Composable
    private fun pbCreationSection() {

        pbCreationActions()
        Spacer(modifier = Modifier.height(10.dp))
        pbCreationComponents()
    }

    @Composable
    private fun pbCreationActions() {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column {
            Row {
                OutlinedButton(modifier = Modifier
                    .width(200.dp)
                    .height(70.dp),
                    colors = CommonViewComp.getActionsButtonColour(),
                    onClick = {
                        Klog.line("PlanningBookManagerView","PBCreationActions", "create Planning Book button clicked")
                        viewModel.createPlanningBook();
                    }) {
                    Text("Create")
                }

                OutlinedButton(modifier = Modifier
                    .width(200.dp)
                    .height(70.dp),
                    colors = CommonViewComp.getSecondaryButtonColour(),
                    onClick = {
                        Klog.line("PlanningBookManagerView","PBCreationActions","cancel create Planning Book button clicked")
                        viewModel.showPBCreationSection(false);
                    }) {
                    Text("Cancel")
                }
            }
        }
    }

    @Composable
    private fun pbCreationComponents() {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Row {
            Column(
                Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = uiState.pbName,
                    onValueChange = { viewModel.updatePBName(it) },
                    placeholder = { Text("Enter Planning Book Name (5-20)") }
                )
                if (uiState.pbNameError) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        uiState.pbNameErrorText,
                        color = Color.Red,
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color.Red
                        )
                    )
                }
            }
        }
    }

    @Composable
    private fun pbListSection() {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(15.dp)
                    .border(2.dp, color = Gray, shape = RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .fillMaxHeight()
            ){
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 10.dp)
                ) {
                    for(planningBook in uiState.planningBookList) {
                        pbCardComponent(planningBook)
                    }
                }
            }//Box
        }
    }


    @Composable
    private fun pbCardComponent(planningBook: PlanningBook) {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val isActivePB = viewModel.isActivePlanningBook(planningBook)
        val isActiveTxt = if(isActivePB) "is Active" else ""
        val isSharedWithMe = viewModel.isSharedWithMePlanningBook(planningBook)
        Klog.line("PlanningBookManagerView","pbCardComponent","isActiveTxt: $isActiveTxt")

        OutlinedCard(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth()
                .height(calcCardHeight(planningBook, uiState))
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            colors = CommonViewComp.gePlanningBookCardColour(),
            elevation = CardDefaults.outlinedCardElevation(),
            border = CardDefaults.outlinedCardBorder(),
        )
        {
            Column(Modifier.padding(4.dp)
                           .width(550.dp),
                   horizontalAlignment = Alignment.Start)
            {
                pbCardComponentRowName(planningBook, isActivePB, isActiveTxt)

                Spacer(modifier = Modifier.height(20.dp))

                if(uiState.isSharing && uiState.isSharingPB == planningBook.id)  {
                    pbCardComponentSharingPB(planningBook)
                }
                else {
                    pbCardComponentRowButtons(planningBook, isSharedWithMe)
                }
            }//Column
        }//card
    }

    private fun calcCardHeight(planningBook: PlanningBook, uiState: PlanningBookManagerUiState): Dp {

        var cardHeight: Dp

        if(uiState.isSharing && uiState.isSharingPB == planningBook.id)
            if(uiState.shareToEmailError)
                cardHeight = 250.dp
            else
                cardHeight = 220.dp
        else
            cardHeight = 110.dp

        return cardHeight
    }

    @Composable
    private fun pbCardComponentRowName(planningBook: PlanningBook,
                                       isActivePB: Boolean,
                                       isActiveTxt: String) {

        Row (Modifier.fillMaxWidth(),
             horizontalArrangement = Arrangement.SpaceBetween)
        {
            Klog.line("PlanningBookManagerView","pbCardComponentRowName","row 1")
            Column(Modifier.padding(4.dp))
            {
                Text(
                    text = planningBook.name,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Column(Modifier.padding(4.dp))
            {
                if(isActivePB) {
                    pbCardComponentTxtActive(isActiveTxt)
                }
                else {
                    pbCardComponentButtonActive(planningBook)
                }
            }
        }
    }

    @Composable
    private fun pbCardComponentTxtActive(isActiveTxt: String) {

        Text(
            text = isActiveTxt,
            style = MaterialTheme.typography.titleMedium
        )
    }

    @Composable
    private fun pbCardComponentButtonActive(planningBook: PlanningBook) {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        OutlinedButton(modifier = Modifier
            .width(110.dp)
            .height(35.dp),
            colors = CommonViewComp.getPlanningBookCardButtonPrimaryColour(),
            onClick = {
                Klog.line("PlanningBookManagerView","pbCardComponentButtonActive","set Active PlanningBook button clicked")
                viewModel.setActivePlanningBook(planningBook.id)
            }
        ) {
            Text(
                "Activate",
                style = MaterialTheme.typography.titleMedium,
            )
        }//button
    }

    @Composable
    private fun pbCardComponentRowButtons(planningBook: PlanningBook, isSharedWithMe: Boolean) {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()


        if(isSharedWithMe) {
            Row ( Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "This planning book is shared",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedButton(modifier = Modifier.width(110.dp).height(35.dp),
                    colors = CommonViewComp.getPlanningBookCardButtonSecondaryColour(),
                    onClick = {
                        Klog.line("PlanningBookManagerView","pbCardComponentRowButtons", "forget PlanningBook button clicked")
                        viewModel.forgetPlanningBook(planningBook)
                    }) {
                    Text(
                        "Forget",
                        style = MaterialTheme.typography.titleMedium,
                    )
                } //OutlinedButton
            }
        }
        else {
            Row ( Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(modifier = Modifier.width(110.dp).height(35.dp),
                    colors = CommonViewComp.getPlanningBookCardButtonPrimaryColour(),
                    onClick = {
                        Klog.line("PlanningBookManagerView","pbCardComponentRowButtons","share PlanningBook button clicked")
                        viewModel.sharePlanningBook_ON(planningBook.id)
                    }) {
                    Text(
                        "Share",
                        style = MaterialTheme.typography.titleMedium,
                    )
                } //OutlinedButton

                OutlinedButton(modifier = Modifier
                    .width(110.dp)
                    .height(35.dp),
                    colors = CommonViewComp.getPlanningBookCardButtonSecondaryColour(),
                    onClick = {
                        Klog.line("PlanningBookManagerView","pbCardComponentRowButtons","delete PlanningBook button clicked")
                        viewModel.deletePlanningBook(planningBook.id)
                    }
                ) {
                    Text(
                        "Remove",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }//OutlinedButton
            }//Row
        }
    }

    @Composable
    private fun pbCardComponentSharingPB(planningBook: PlanningBook) {

        val viewModel: PlanningBookManagerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Box(
            modifier = Modifier
                .padding(15.dp)
                .border(2.dp, color = Gray, shape = RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column {

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        modifier = Modifier.width(110.dp).height(35.dp),
                        colors = CommonViewComp.getActionsButtonColour(),
                        onClick = {
                            Klog.line("PlanningBookManagerView", "pbCardComponentSharingRowSection", "share the planningBook to")
                            viewModel.sharePlanningBook(planningBook);
                        }) {
                        Text("Share")
                    }

                    OutlinedButton(
                        modifier = Modifier.width(110.dp).height(35.dp),
                        colors = CommonViewComp.getSecondaryButtonColour(),
                        onClick = {
                            Klog.line("PlanningBookManagerView", "pbCardComponentSharingRowSection", "cancel sharing planningBook to")
                            viewModel.sharePlanningBook_OFF()
                        }) {
                        Text("Cancel")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row (
                    Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        OutlinedTextField(
                            value = uiState.shareToEmail,
                            onValueChange = { viewModel.updateShareToEmail(it) },
                            placeholder = { Text("Person Email to share with") })
                        if(uiState.shareToEmailError) {
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                uiState.shareToEmailErrorText, color = Color.Red, style = TextStyle(
                                    fontSize = 15.sp, color = Color.Red
                                )
                            )
                        }
                    }
                }
            }
        }//Box
    }
}