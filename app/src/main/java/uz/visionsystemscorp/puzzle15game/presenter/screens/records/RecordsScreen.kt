package uz.visionsystemscorp.puzzle15game.presenter.screens.records

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.domain.model.Leaderboard
import uz.visionsystemscorp.puzzle15game.navigation.RecordsRoute
import uz.visionsystemscorp.puzzle15game.presenter.screens.records.RecordsContract.RecordsEvent
import uz.visionsystemscorp.puzzle15game.presenter.screens.records.RecordsContract.RecordsTab
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.LeaderboardEntry
import uz.visionsystemscorp.puzzle15game.domain.model.LeaderboardStatus
import uz.visionsystemscorp.puzzle15game.domain.model.PersonalStats
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.popIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.pulse
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.slideIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ImageIconButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.OutlinedText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ScreenBackground
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.StatBox
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.StatBoxStyle
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.TopLeftHomeButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.TopSign
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodPanel
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodTabs
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.place
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.BarkDeep
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bronze
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Cream
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Gold
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.GoldLight
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LabelStyle
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LeafBright
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LeafDark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Silver
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.formatElapsed
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.label

private val Medals = listOf(Gold, Silver, Bronze)

@Composable
fun RecordsScreen(route: RecordsRoute) {
    val viewModel: RecordsContract.RecordsViewModel =
        hiltViewModel<RecordsViewModel, RecordsViewModel.Factory>(creationCallback = { it.create(route) })
    val uiState = viewModel.collectAsState()
    RecordsContent(uiState = uiState.value, onEventDispatcher = viewModel::onEventDispatcher)
}

@Composable
internal fun RecordsContent(
    uiState: RecordsContract.RecordsUiState,
    onEventDispatcher: (RecordsEvent) -> Unit,
) {
    ScreenBackground(scrimAlpha = 0.42f) {
        TopSign(stringResource(R.string.records_title), fontSize = 28.sp)
        TopLeftHomeButton { onEventDispatcher(RecordsEvent.HomeClicked) }

        WoodTabs(
            items = BoardSize.entries,
            selected = uiState.size,
            onSelect = { onEventDispatcher(RecordsEvent.SizeSelected(it)) },
            label = { it.label() },
            tabSize = DpSize(108.dp, 49.dp),
            fontSize = 22.sp,
            modifier = Modifier.offset(y = 172.dp).fillMaxWidth(),
        )
        WoodTabs(
            items = RecordsTab.entries,
            selected = uiState.tab,
            onSelect = { onEventDispatcher(RecordsEvent.TabSelected(it)) },
            label = {
                stringResource(if (it == RecordsTab.World) R.string.records_world else R.string.records_mine)
            },
            tabSize = DpSize(160.dp, 58.dp),
            fontSize = 20.sp,
            modifier = Modifier.offset(y = 230.dp).fillMaxWidth(),
            spacing = 10.dp,
        )

        WoodPanel(Modifier.place(15.dp, 300.dp, 360.dp, 336.dp)) {
            key(uiState.size, uiState.tab) {
                Box(Modifier.padding(horizontal = 2.dp, vertical = 4.dp)) {
                    when (uiState.tab) {
                        RecordsTab.World -> WorldTable(uiState.leaderboard)
                        RecordsTab.Mine -> uiState.personal?.let { PersonalGrid(it) } ?: EmptyMessage(stringResource(R.string.records_mine_empty))
                    }
                }
            }
        }

        WoodPanel(
            modifier = Modifier.place(15.dp, 648.dp, 360.dp, 82.dp),
            contentAlignment = Alignment.Center,
        ) {
            when (uiState.tab) {
                RecordsTab.World -> uiState.leaderboard.you?.let { YourRow(it) }
                RecordsTab.Mine -> uiState.personal?.let { stats ->
                    stats.percentile?.let { Percentile(it) }
                        ?: TableValue(stringResource(R.string.records_total_stars, stats.totalStars), Cream)
                }
            }
        }

        ImageIconButton(
            icon = R.drawable.btn_play_txt,
            contentDescription = stringResource(R.string.cd_play),
            onClick = { onEventDispatcher(RecordsEvent.PlayClicked) },
            modifier = Modifier.place(105.dp, 744.dp, 180.dp, 88.dp).pulse(),
        )
    }
}

@Composable
private fun EmptyMessage(text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        OutlinedText(text, fontSize = 20.sp, shadowColor = null, letterSpacing = 1.sp)
    }
}

@Composable
private fun RecordColumns(
    modifier: Modifier = Modifier,
    rankWidth: Dp = 40.dp,
    rank: @Composable () -> Unit,
    name: @Composable RowScope.() -> Unit,
    moves: @Composable () -> Unit,
    time: @Composable () -> Unit,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(rankWidth)) { rank() }
        name()
        Box(Modifier.width(62.dp), contentAlignment = Alignment.CenterEnd) { moves() }
        Box(Modifier.width(56.dp), contentAlignment = Alignment.CenterEnd) { time() }
    }
}

@Composable
private fun TableValue(text: String, color: Color, fontSize: TextUnit = 18.sp, shadow: Color = BarkDeep) {
    OutlinedText(
        text = text,
        fontSize = fontSize,
        color = color,
        outlineColor = null,
        shadowColor = shadow,
        shadowOffset = 2.dp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Start,
        maxLines = 1,
    )
}

@Composable
private fun WorldTable(leaderboard: Leaderboard) {
    val entries = leaderboard.entries
    if (entries.isEmpty()) {
        val message = when (leaderboard.status) {
            LeaderboardStatus.LOADING -> R.string.records_world_loading
            LeaderboardStatus.OFFLINE -> R.string.records_world_offline
            LeaderboardStatus.READY -> R.string.records_world_empty
        }
        EmptyMessage(stringResource(message))
        return
    }
    Column {
        RecordColumns(
            modifier = Modifier.padding(start = 6.dp, end = 6.dp, bottom = 8.dp),
            rank = { Text(stringResource(R.string.records_rank), style = LabelStyle) },
            name = { Text(stringResource(R.string.records_player), style = LabelStyle, modifier = Modifier.weight(1f)) },
            moves = { Text(stringResource(R.string.label_moves), style = LabelStyle) },
            time = { Text(stringResource(R.string.label_time), style = LabelStyle) },
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            entries.forEachIndexed { i, entry ->
                RecordColumns(
                    modifier = Modifier
                        .slideIn(delayMillis = i * 70, offsetX = (-30).dp, offsetY = 0.dp, durationMillis = 400)
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(Bark.copy(alpha = if (i % 2 == 0) 0.6f else 0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp),
                    rank = { entry.rank?.let { RankBadge(it) } },
                    name = { Box(Modifier.weight(1f)) { TableValue(entry.playerName, Cream) } },
                    moves = { TableValue(entry.moves.toString(), GoldLight) },
                    time = { TableValue(formatElapsed(entry.seconds), Cream) },
                )
            }
        }
    }
}

@Composable
private fun RankBadge(rank: Int) {
    val medal = Medals.getOrNull(rank - 1)
    Box(
        modifier = Modifier
            .size(28.dp)
            .then(if (medal != null) Modifier.background(medal, CircleShape).border(2.dp, Bark, CircleShape) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        TableValue(rank.toString(), if (medal != null) Bark else Cream, fontSize = 15.sp, shadow = Color.Transparent)
    }
}

@Composable
private fun YourRow(you: LeaderboardEntry) {
    RecordColumns(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        rankWidth = 48.dp,
        rank = {
            val rank = you.rank?.let { stringResource(R.string.records_rank_value, it) } ?: stringResource(R.string.records_rank_unknown)
            TableValue(rank, LeafBright, shadow = LeafDark)
        },
        name = {
            Box(Modifier.weight(1f)) {
                TableValue(stringResource(R.string.records_you), LeafBright, fontSize = 20.sp, shadow = LeafDark)
            }
        },
        moves = { TableValue(you.moves.toString(), GoldLight) },
        time = { TableValue(formatElapsed(you.seconds), Cream) },
    )
}

@Composable
private fun PersonalGrid(stats: PersonalStats) {
    val cards = listOf(
        stringResource(R.string.records_best_moves) to stats.bestMoves.toString(),
        stringResource(R.string.records_best_time) to formatElapsed(stats.bestSeconds),
        stringResource(R.string.records_games) to stats.gamesPlayed.toString(),
        stringResource(R.string.records_avg_moves) to stats.averageMoves.toString(),
    )
    Column(Modifier.padding(top = 6.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        cards.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { (label, value) ->
                    StatBox(
                        label = label,
                        value = value,
                        modifier = Modifier.weight(1f).height(118.dp).popIn(initialScale = 0.6f),
                        style = StatBoxStyle.Inset,
                        valueFontSize = 34.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun Percentile(percent: Int) {
    val value = stringResource(R.string.records_percent, percent)
    val text = stringResource(R.string.records_better_than, value)
    val start = text.indexOf(value)
    Row {
        listOf(
            text.substring(0, start) to Cream,
            value to LeafBright,
            text.substring(start + value.length) to Cream,
        ).forEach { (part, color) ->
            if (part.isNotEmpty()) TableValue(part, color)
        }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun RecordsScreenPreview() {
    Puzzle15Theme {
        RecordsContent(
            uiState = RecordsContract.RecordsUiState(
                leaderboard = Leaderboard(
                    entries = listOf(
                        LeaderboardEntry(1, "Aziza", 48, 41),
                        LeaderboardEntry(2, "Bekzod", 52, 47),
                        LeaderboardEntry(3, "Malika", 57, 55),
                        LeaderboardEntry(4, "Sardor", 61, 58),
                    ),
                    you = LeaderboardEntry(24, "Adilxan", 87, 102),
                    status = LeaderboardStatus.READY,
                ),
            ),
            onEventDispatcher = {},
        )
    }
}
