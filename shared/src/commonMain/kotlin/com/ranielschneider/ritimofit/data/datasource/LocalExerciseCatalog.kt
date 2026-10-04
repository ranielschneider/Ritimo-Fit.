package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.domain.model.Exercise

object LocalExerciseCatalog {
    val exercises: List<Exercise> = listOf(
        // --- PEITO (12) ---
        Exercise(
            id = "ex_peito_01",
            name = "Supino Reto com Barra",
            muscleGroup = "Peito",
            equipment = "Barra e Banco Reto",
            description = "Exercício multiarticular fundamental para desenvolvimento do peitoral maior, tríceps e deltoide anterior.",
            instructions = listOf(
                "Deite-se no banco reto com os pés apoiados firmemente no chão.",
                "Segure a barra com uma pegada ligeiramente mais larga que a largura dos ombros.",
                "Desça a barra de forma controlada até tocar suavemente a linha do peito.",
                "Empurre a barra para cima até a extensão completa dos braços sem travar os cotovelos."
            )
        ),
        Exercise(
            id = "ex_peito_02",
            name = "Supino Inclinado com Halteres",
            muscleGroup = "Peito",
            equipment = "Halteres e Banco Inclinado",
            description = "Focado no desenvolvimento da porção clavicular (superior) do peitoral maior.",
            instructions = listOf(
                "Ajuste o banco em um ângulo entre 30° e 45°.",
                "Posicione os halteres na altura do peito com a pegada pronada.",
                "Empurre os halteres para cima alinhados com o peitoral superior.",
                "Retorne devagar sentindo o alongamento do peitoral."
            )
        ),
        Exercise(
            id = "ex_peito_03",
            name = "Supino Declinado com Barra",
            muscleGroup = "Peito",
            equipment = "Barra e Banco Declinado",
            description = "Enfatiza a porção esternal e inferior do peitoral maior.",
            instructions = listOf(
                "Fixe os pés nos apoios do banco declinado.",
                "Retire a barra do suporte e desça controladamente até a parte inferior do tórax.",
                "Empurre a barra verticalmente até estender os braços."
            )
        ),
        Exercise(
            id = "ex_peito_04",
            name = "Crucifixo Reto com Halteres",
            muscleGroup = "Peito",
            equipment = "Halteres e Banco Reto",
            description = "Exercício de isolamento para máximo alongamento das fibras do peitoral.",
            instructions = listOf(
                "Deite-se no banco segurando os halteres acima do peito com palmas viradas para dentro.",
                "Abra os braços suavemente em um arco com os cotovelos levemente dobrados.",
                "Retorne juntando os halteres no topo na mesma trajetória de arco."
            )
        ),
        Exercise(
            id = "ex_peito_05",
            name = "Crucifixo Inclinado com Halteres",
            muscleGroup = "Peito",
            equipment = "Halteres e Banco Inclinado",
            description = "Isolamento da porção superior do peitoral com halteres.",
            instructions = listOf(
                "Ajuste o banco inclinado a 30°.",
                "Abra os braços lateralmente até sentir bom alongamento no peitoral superior.",
                "Feche os braços retornando os pesos ao topo sem chocá-los."
            )
        ),
        Exercise(
            id = "ex_peito_06",
            name = "Crossover na Polia Alta",
            muscleGroup = "Peito",
            equipment = "Cabo/polia",
            description = "Trabalha a porção inferior e média do peitoral com tensão constante do cabo.",
            instructions = listOf(
                "Posicione as polias na posição alta e dê um passo à frente com um pé.",
                "Puxe as manoplas para baixo e para a frente até se cruzarem na linha da cintura.",
                "Retorne devagar controlando a resistência das polias."
            )
        ),
        Exercise(
            id = "ex_peito_07",
            name = "Crossover na Polia Baixa",
            muscleGroup = "Peito",
            equipment = "Cabo/polia",
            description = "Enfatiza a porção superior do peitoral.",
            instructions = listOf(
                "Ajuste as polias na posição mais baixa.",
                "Puxe as manoplas para cima e para o centro até a altura do peitoral superior.",
                "Desça devagar acompanhando o cabo."
            )
        ),
        Exercise(
            id = "ex_peito_08",
            name = "Peck Deck / Voador",
            muscleGroup = "Peito",
            equipment = "Máquina",
            description = "Isolamento mecânico seguro para peitoral maior.",
            instructions = listOf(
                "Ajuste o assento para que os braços fiquem na linha dos ombros.",
                "Junte as hastes da máquina à frente do peito contraindo o peitoral.",
                "Abra devagar até o alinhamento dos ombros."
            )
        ),
        Exercise(
            id = "ex_peito_09",
            name = "Flexão de Braços Solo",
            muscleGroup = "Peito",
            equipment = "Peso corporal",
            description = "Exercício calistênico clássico para peito, tríceps e estabilidade abdominal.",
            instructions = listOf(
                "Apoie as mãos no chão ligeiramente além da largura dos ombros.",
                "Mantenha o corpo reto da cabeça aos calcanhares.",
                "Desça o peito até próximo ao chão e empurre de volta."
            )
        ),
        Exercise(
            id = "ex_peito_10",
            name = "Flexão Inclinada (Pés Elevados)",
            muscleGroup = "Peito",
            equipment = "Peso corporal e Banco",
            description = "Aumenta a carga na porção superior do peitoral e ombros.",
            instructions = listOf(
                "Apoie os pés sobre um banco e as mãos no chão.",
                "Desça o peito em direção ao solo mantendo o core firme.",
                "Empurre até a extensão completa dos braços."
            )
        ),
        Exercise(
            id = "ex_peito_11",
            name = "Chest Press Articulado",
            muscleGroup = "Peito",
            equipment = "Máquina",
            description = "Desenvolvimento de força no peitoral com trajetória guiada e segura.",
            instructions = listOf(
                "Ajuste a altura do banco para que as manoplas fiquem na altura do peito.",
                "Empurre as manoplas até quase estender totalmente os braços.",
                "Retorne controladamente."
            )
        ),
        Exercise(
            id = "ex_peito_12",
            name = "Pullover com Halter no Banco",
            muscleGroup = "Peito",
            equipment = "Halter e Banco Reto",
            description = "Trabalha peitoral maior, serrátil anterior e expansão da caixa torácica.",
            instructions = listOf(
                "Deite-se no banco apoiando a parte superior das costas.",
                "Segure um halter com as duas mãos acima do peito.",
                "Desça o halter para trás da cabeça mantendo os cotovelos levemente flexionados.",
                "Puxe o halter de volta até a vertical sobre o peito."
            )
        ),

        // --- COSTAS (14) ---
        Exercise(
            id = "ex_costas_01",
            name = "Barra Fixa Pegada Pronada",
            muscleGroup = "Costas",
            equipment = "Barra / Peso corporal",
            description = "Trabalha fortemente o latíssimo do dorso e dorsal superior.",
            instructions = listOf(
                "Segure a barra fixa com pegada aberta e palmas viradas para a frente.",
                "Puxe o corpo para cima até o queixo ultrapassar a linha da barra.",
                "Desça de forma controlada até estender quase completamente os braços."
            )
        ),
        Exercise(
            id = "ex_costas_02",
            name = "Barra Fixa Pegada Supinada (Chin-Up)",
            muscleGroup = "Costas",
            equipment = "Barra / Peso corporal",
            description = "Envolve dorsal inferior e grande ativação do bíceps braquial.",
            instructions = listOf(
                "Segure a barra com palmas viradas para você na largura dos ombros.",
                "Puxe o corpo para cima focando em puxar com os cotovelos.",
                "Retorne suavemente até a extensão dos braços."
            )
        ),
        Exercise(
            id = "ex_costas_03",
            name = "Puxada Frontal no Pulley Pegada Aberta",
            muscleGroup = "Costas",
            equipment = "Máquina / Cabo",
            description = "Exercício chave para ganho de largura nas costas.",
            instructions = listOf(
                "Sente-se na máquina e encaixe as coxas nos apoios.",
                "Puxe a barra em direção ao peito superior projetando o tórax para a frente.",
                "Retorne a barra suavemente subindo os braços."
            )
        ),
        Exercise(
            id = "ex_costas_04",
            name = "Puxada com Triângulo no Pulley",
            muscleGroup = "Costas",
            equipment = "Máquina / Cabo",
            description = "Pegada neutra e fechada para enfatizar a região central das costas.",
            instructions = listOf(
                "Acople o puxador triângulo na polia alta.",
                "Puxe o triângulo até tocar o peito, contraindo a musculatura dorsal.",
                "Retorne acompanhando a carga."
            )
        ),
        Exercise(
            id = "ex_costas_05",
            name = "Remada Curvada com Barra",
            muscleGroup = "Costas",
            equipment = "Barra Livre",
            description = "Desenvolve espessura e densidade na musculatura das costas.",
            instructions = listOf(
                "Flexione os joelhos levemente e incline o tronco a 45° com a coluna neutra.",
                "Puxe a barra até a região do umbigo mantendo os cotovelos retrocedidos.",
                "Abaixe a barra com controle sem perder a postura."
            )
        ),
        Exercise(
            id = "ex_costas_06",
            name = "Remada Unilateral com Halter (Serrote)",
            muscleGroup = "Costas",
            equipment = "Halter e Banco Reto",
            description = "Permite trabalho isolado de cada lado da dorsal com grande amplitude.",
            instructions = listOf(
                "Apoie um joelho e mão no banco reto.",
                "Com a outra mão, puxe o halter ao lado do quadril elevando o cotovelo.",
                "Desça o halter até estender completamente o braço."
            )
        ),
        Exercise(
            id = "ex_costas_07",
            name = "Remada Baixa no Cabo com Triângulo",
            muscleGroup = "Costas",
            equipment = "Cabo/polia",
            description = "Tensão contínua para miolo de costas, romboides e dorsal.",
            instructions = listOf(
                "Sente-se de frente para a polia baixa com pés apoiados.",
                "Puxe o triângulo em direção ao abdômen mantendo a coluna ereta.",
                "Retorne estendendo os braços sem curvar a lombar."
            )
        ),
        Exercise(
            id = "ex_costas_08",
            name = "Remada Articulada Cavalinho",
            muscleGroup = "Costas",
            equipment = "Máquina / Barra T",
            description = "Trabalho pesado de espessura de costas com apoio estável.",
            instructions = listOf(
                "Posicione-se na barra T ou máquina cavalinho.",
                "Puxe o peso em direção ao tórax contraindo forte as costas.",
                "Desça o peso controlando o movimento."
            )
        ),
        Exercise(
            id = "ex_costas_09",
            name = "Remada Peitoral no Banco Inclinado",
            muscleGroup = "Costas",
            equipment = "Halteres e Banco Inclinado",
            description = "Remove a carga da lombar, isolando a musculatura torácica posterior.",
            instructions = listOf(
                "Deite-se de bruços em um banco inclinado a 30°.",
                "Puxe os halteres trazendo os cotovelos para cima.",
                "Desça os halteres devagar."
            )
        ),
        Exercise(
            id = "ex_costas_10",
            name = "Pulldown no Cabo com Barra Reta",
            muscleGroup = "Costas",
            equipment = "Cabo/polia",
            description = "Isolamento da grande dorsal sem participação marcante do bíceps.",
            instructions = listOf(
                "Fique em pé de frente para a polia alta segurando a barra reta.",
                "Com os cotovelos levemente flexionados, puxe a barra para baixo até as coxas.",
                "Retorne a barra até a altura dos olhos."
            )
        ),
        Exercise(
            id = "ex_costas_11",
            name = "Remada na Máquina Pegada Neutra",
            muscleGroup = "Costas",
            equipment = "Máquina",
            description = "Trajetória biomecânica perfeita para trabalho de dorsal média.",
            instructions = listOf(
                "Ajuste o apoio do peito no suporte da máquina.",
                "Puxe as manoplas para trás aproximando as escápulas.",
                "Retorne suavemente."
            )
        ),
        Exercise(
            id = "ex_costas_12",
            name = "Extensão Lombar (Banco Romano)",
            muscleGroup = "Costas",
            equipment = "Banco Romano / Peso corporal",
            description = "Fortalecimento dos eretores da espinha e estabilização lombar.",
            instructions = listOf(
                "Posicione o quadril no suporte do banco romano.",
                "Incline o tronco para baixo e suba até alinhar com as pernas.",
                "Evite hiperextender excessivamente a coluna no topo."
            )
        ),
        Exercise(
            id = "ex_costas_13",
            name = "Levantamento Terra com Barra",
            muscleGroup = "Costas",
            equipment = "Barra Livre",
            description = "Exercício básico de força para cadeia posterior completa.",
            instructions = listOf(
                "Posicione a barra sobre a metade dos pés e mantenha a coluna neutra.",
                "Flexione joelhos e quadril para segurar a barra.",
                "Suba empurrando o chão e alinhando o quadril no topo.",
                "Desça mantendo o peso próximo às pernas."
            )
        ),
        Exercise(
            id = "ex_costas_14",
            name = "Remada no Cabo com Corda",
            muscleGroup = "Costas",
            equipment = "Cabo/polia",
            description = "Permite maior rotação e contração dos romboides no final.",
            instructions = listOf(
                "Fixe a corda na polia média ou baixa.",
                "Puxe separando as pontas da corda na linha do abdômen.",
                "Retorne juntando as mãos à frente."
            )
        ),

        // --- OMBROS (12) ---
        Exercise(
            id = "ex_ombro_01",
            name = "Desenvolvimento com Halteres Sentado",
            muscleGroup = "Ombros",
            equipment = "Halteres e Banco",
            description = "Construção de massa e força geral para o deltoide.",
            instructions = listOf(
                "Sente-se com as costas apoiadas no banco vertical.",
                "Eleve os halteres até a linha das orelhas.",
                "Empurre verticalmente para cima até quase tocar os halteres.",
                "Desça com controle até a altura das orelhas."
            )
        ),
        Exercise(
            id = "ex_ombro_02",
            name = "Desenvolvimento Militar com Barra em Pé",
            muscleGroup = "Ombros",
            equipment = "Barra Livre",
            description = "Exercício fundamental de força de empurrar vertical.",
            instructions = listOf(
                "Mantenha os pés na largura dos quadris e segure a barra no peitoral superior.",
                "Empurre a barra para cima passando rente ao rosto.",
                "Bloqueie o movimento acima da cabeça e desça com segurança."
            )
        ),
        Exercise(
            id = "ex_ombro_03",
            name = "Elevação Lateral com Halteres",
            muscleGroup = "Ombros",
            equipment = "Halteres",
            description = "Isolamento da porção lateral do deltoide.",
            instructions = listOf(
                "Mantenha o corpo reto e halteres ao lado das coxas.",
                "Eleve os braços lateralmente até a altura dos ombros.",
                "Desça o peso devagar sentindo a resistência."
            )
        ),
        Exercise(
            id = "ex_ombro_04",
            name = "Elevação Lateral na Polia Baixa",
            muscleGroup = "Ombros",
            equipment = "Cabo/polia",
            description = "Oferece tensão contínua durante todo o arco do movimento.",
            instructions = listOf(
                "Posicione a polia na altura do tornozelo.",
                "Puxe o cabo cruzando à frente do corpo até a altura do ombro.",
                "Desça devagar acompanhando a polia."
            )
        ),
        Exercise(
            id = "ex_ombro_05",
            name = "Elevação Frontal com Halteres",
            muscleGroup = "Ombros",
            equipment = "Halteres",
            description = "Trabalha a porção anterior do deltoide.",
            instructions = listOf(
                "Segure os halteres à frente das coxas com pegada pronada.",
                "Eleve um braço por vez até a linha dos olhos.",
                "Retorne suavemente."
            )
        ),
        Exercise(
            id = "ex_ombro_06",
            name = "Elevação Frontal na Polia com Corda",
            muscleGroup = "Ombros",
            equipment = "Cabo/polia",
            description = "Foco em deltoide anterior com cabo passando entre as pernas.",
            instructions = listOf(
                "Posicione a polia baixa e passe a corda entre as pernas.",
                "Eleve os braços à frente até a altura dos ombros.",
                "Desça de forma controlada."
            )
        ),
        Exercise(
            id = "ex_ombro_07",
            name = "Crucifixo Invertido com Halteres",
            muscleGroup = "Ombros",
            equipment = "Halteres",
            description = "Foco no deltoide posterior e estabilizadores das escápulas.",
            instructions = listOf(
                "Incline o tronco para a frente mantendo a coluna neutra.",
                "Abra os braços lateralmente elevando os cotovelos.",
                "Desça os halteres devagar sem balançar."
            )
        ),
        Exercise(
            id = "ex_ombro_08",
            name = "Crucifixo Invertido no Peck Deck",
            muscleGroup = "Ombros",
            equipment = "Máquina",
            description = "Isolamento do deltoide posterior na máquina guiada.",
            instructions = listOf(
                "Sente-se de frente para o encosto do Peck Deck.",
                "Empurre as hastes para trás com os braços estendidos.",
                "Retorne suavemente à posição inicial."
            )
        ),
        Exercise(
            id = "ex_ombro_09",
            name = "Face Pull no Cabo com Corda",
            muscleGroup = "Ombros",
            equipment = "Cabo/polia",
            description = "Excelente para deltoide posterior, manguito rotador e postura.",
            instructions = listOf(
                "Ajuste a polia na altura do rosto.",
                "Puxe a corda em direção ao nariz separando as mãos e rodando os ombros para fora.",
                "Retorne estendendo os braços."
            )
        ),
        Exercise(
            id = "ex_ombro_10",
            name = "Elevação Y no Banco Inclinado",
            muscleGroup = "Ombros",
            equipment = "Halteres e Banco Inclinado",
            description = "Ativação de deltoide e trapézio inferior.",
            instructions = listOf(
                "Deite-se de bruços em banco a 45°.",
                "Eleve os halteres em formato da letra Y.",
                "Desça lentamente."
            )
        ),
        Exercise(
            id = "ex_ombro_11",
            name = "Desenvolvimento no Smith",
            muscleGroup = "Ombros",
            equipment = "Smith",
            description = "Trabalho pesado de desenvolvimento com trajetória fixa.",
            instructions = listOf(
                "Posicione o banco no centro do Smith.",
                "Desça a barra até a altura do queixo e empurre para cima."
            )
        ),
        Exercise(
            id = "ex_ombro_12",
            name = "Arnold Press com Halteres",
            muscleGroup = "Ombros",
            equipment = "Halteres e Banco",
            description = "Variação dinâmica com rotação do punho que trabalha todas as cabeças do deltoide.",
            instructions = listOf(
                "Inicie com os halteres na frente do peito com palmas viradas para você.",
                "Ao empurrar para cima, gire as mãos para fora até terminar em pegada pronada.",
                "Inverta a rotação ao descer."
            )
        ),

        // --- TRAPÉZIO E ANTEBRAÇO (8) ---
        Exercise(
            id = "ex_trap_01",
            name = "Encolhimento com Halteres",
            muscleGroup = "Trapézio",
            equipment = "Halteres",
            description = "Isolamento da porção superior do trapézio.",
            instructions = listOf(
                "Segure os halteres ao lado do corpo com postura ereta.",
                "Eleve os ombros em direção às orelhas sem girá-los.",
                "Contraia por 1 segundo no topo e desça com controle."
            )
        ),
        Exercise(
            id = "ex_trap_02",
            name = "Encolhimento com Barra por Trás",
            muscleGroup = "Trapézio",
            equipment = "Barra Livre",
            description = "Variação de encolhimento para hipertrofia de trapézio superior e médio.",
            instructions = listOf(
                "Segure a barra atrás das coxas.",
                "Eleve os ombros verticalmente contraindo o trapézio.",
                "Desça de forma suave."
            )
        ),
        Exercise(
            id = "ex_trap_03",
            name = "Encolhimento no Smith",
            muscleGroup = "Trapézio",
            equipment = "Smith",
            description = "Encolhimento com carga guiada.",
            instructions = listOf(
                "Destrave a barra no Smith na altura das coxas.",
                "Eleve os ombros em direção ao teto e desça devagar."
            )
        ),
        Exercise(
            id = "ex_anteb_01",
            name = "Rosca Inversa com Barra W",
            muscleGroup = "Antebraço",
            equipment = "Barra W",
            description = "Fortalecimento do braquiorradial e extensores do punho.",
            instructions = listOf(
                "Segure a barra W com pegada pronada (palmas para baixo).",
                "Flexione os cotovelos trazendo a barra para cima.",
                "Desça de forma lenta."
            )
        ),
        Exercise(
            id = "ex_anteb_02",
            name = "Rosca Punho com Barra",
            muscleGroup = "Antebraço",
            equipment = "Barra e Banco",
            description = "Fortalecimento específico dos flexores do punho.",
            instructions = listOf(
                "Apoie os antebraços no banco com as mãos para fora.",
                "Flexione os punhos para cima e desça alongando os dedos."
            )
        ),
        Exercise(
            id = "ex_anteb_03",
            name = "Rosca Punho Inversa com Halteres",
            muscleGroup = "Antebraço",
            equipment = "Halteres e Banco",
            description = "Trabalha a musculatura extensora do punho.",
            instructions = listOf(
                "Apoie os antebraços com palmas viradas para baixo.",
                "Eleve os punhos para cima e retorne suavemente."
            )
        ),
        Exercise(
            id = "ex_anteb_04",
            name = "Caminhada do Fazendeiro (Farmer Walk)",
            muscleGroup = "Antebraço",
            equipment = "Halteres / Kettlebell",
            description = "Trabalho funcional pesado para pegada, antebraço e estabilização de core.",
            instructions = listOf(
                "Segure halteres pesados um em cada mão.",
                "Caminhe a passos firmes mantendo a postura perfeita e ombros para trás."
            )
        ),
        Exercise(
            id = "ex_anteb_05",
            name = "Flexão de Punho na Polia",
            muscleGroup = "Antebraço",
            equipment = "Cabo/polia",
            description = "Tensão constante nos flexores do punho.",
            instructions = listOf(
                "Ajoelhe-se em frente à polia baixa com barra reta.",
                "Flexione o punho puxando o cabo para cima."
            )
        ),

        // --- BÍCEPS (10) ---
        Exercise(
            id = "ex_biceps_01",
            name = "Rosca Direta com Barra W",
            muscleGroup = "Bíceps",
            equipment = "Barra W",
            description = "Exercício base com pegada anatômica para redução de estresse nos punhos.",
            instructions = listOf(
                "Segure a barra W na largura dos ombros.",
                "Eleve a barra flexionando os cotovelos sem movimentar o tronco.",
                "Desça lentamente estendendo o braço."
            )
        ),
        Exercise(
            id = "ex_biceps_02",
            name = "Rosca Alternada com Halteres",
            muscleGroup = "Bíceps",
            equipment = "Halteres",
            description = "Permite foco individualizado e supinação durante o movimento.",
            instructions = listOf(
                "Com halteres nas mãos, suba um braço girando a palma para cima.",
                "Contraia o bíceps no topo e desça enquanto o outro braço sobe."
            )
        ),
        Exercise(
            id = "ex_biceps_03",
            name = "Rosca Martelo com Halteres",
            muscleGroup = "Bíceps",
            equipment = "Halteres",
            description = "Enfatiza o músculo braquial e braquiorradial (largura do braço).",
            instructions = listOf(
                "Segure os halteres com pegada neutra (palmas viradas para dentro).",
                "Eleve o halter mantendo o pulso firme e neutro.",
                "Desça com controle."
            )
        ),
        Exercise(
            id = "ex_biceps_04",
            name = "Rosca Scott com Barra W",
            muscleGroup = "Bíceps",
            equipment = "Banco Scott e Barra W",
            description = "Isolamento total do bíceps eliminando qualquer roubo com o corpo.",
            instructions = listOf(
                "Apoie os axilas e antebraços no acolchoado do banco Scott.",
                "Suba a barra até a vertical e desça até estender quase completamente o braço."
            )
        ),
        Exercise(
            id = "ex_biceps_05",
            name = "Rosca Scott na Máquina",
            muscleGroup = "Bíceps",
            equipment = "Máquina",
            description = "Isolamento de bíceps com resistência uniforme guiada.",
            instructions = listOf(
                "Ajuste a altura do assento e apoie os braços no suporte.",
                "Flexione os cotovelos trazendo as manoplas e desça suavemente."
            )
        ),
        Exercise(
            id = "ex_biceps_06",
            name = "Rosca Concentrada com Halter",
            muscleGroup = "Bíceps",
            equipment = "Halter e Banco",
            description = "Foco em pico de contração do bíceps.",
            instructions = listOf(
                "Sente-se no banco e apoie o cotovelo na parte interna da coxa.",
                "Eleve o halter em direção ao peito e desça devagar."
            )
        ),
        Exercise(
            id = "ex_biceps_07",
            name = "Rosca Martelo no Cabo com Corda",
            muscleGroup = "Bíceps",
            equipment = "Cabo/polia",
            description = "Trabalho de martelo no cabo com tensão constante.",
            instructions = listOf(
                "Acople a corda na polia baixa.",
                "Puxe a corda em pegada neutra mantendo cotovelos junto ao corpo."
            )
        ),
        Exercise(
            id = "ex_biceps_08",
            name = "Rosca Inclinada com Halteres",
            muscleGroup = "Bíceps",
            equipment = "Halteres e Banco Inclinado",
            description = "Aumenta o alongamento da cabeça longa do bíceps.",
            instructions = listOf(
                "Sente-se em banco inclinado a 45° deixando os braços caírem para trás.",
                "Flexione os cotovelos sem projetá-los para a frente."
            )
        ),
        Exercise(
            id = "ex_biceps_09",
            name = "Rosca Direta no Cabo (Polia Baixa)",
            muscleGroup = "Bíceps",
            equipment = "Cabo/polia",
            description = "Tensão mecânica do início ao fim da repetição.",
            instructions = listOf(
                "Segure a barra reta acoplada na polia baixa.",
                "Execute a flexão dos braços com controle."
            )
        ),
        Exercise(
            id = "ex_biceps_10",
            name = "Rosca 21 com Barra",
            muscleGroup = "Bíceps",
            equipment = "Barra W / Reta",
            description = "Técnica de parcial para grande volume e congestão muscular.",
            instructions = listOf(
                "Faça 7 repetições da metade inferior do movimento.",
                "Faça 7 repetições da metade superior do movimento.",
                "Faça 7 repetições completas em seguida."
            )
        ),

        // --- TRÍCEPS (10) ---
        Exercise(
            id = "ex_triceps_01",
            name = "Tríceps Pulley com Barra Reta",
            muscleGroup = "Tríceps",
            equipment = "Cabo/polia",
            description = "Exercício clássico no cabo para a cabeça lateral e medial do tríceps.",
            instructions = listOf(
                "Mantenha os cotovelos colados ao tronco.",
                "Empurre a barra para baixo até estender totalmente os braços.",
                "Retorne subindo a barra até 90°."
            )
        ),
        Exercise(
            id = "ex_triceps_02",
            name = "Tríceps Corda no Pulley",
            muscleGroup = "Tríceps",
            equipment = "Cabo/polia",
            description = "Permite abrir as pontas da corda na parte inferior maximizando a contração.",
            instructions = listOf(
                "Segure as pontas da corda e empurre para baixo.",
                "Afaste as mãos na parte inferior contraindo forte o tríceps.",
                "Retorne com controle."
            )
        ),
        Exercise(
            id = "ex_triceps_03",
            name = "Tríceps Testa com Barra W",
            muscleGroup = "Tríceps",
            equipment = "Barra W e Banco Reto",
            description = "Grande estímulo para a cabeça longa e medial do tríceps.",
            instructions = listOf(
                "Deite-se no banco e segure a barra W na vertical.",
                "Flexione apenas os cotovelos trazendo a barra em direção à testa.",
                "Estenda os braços voltando à posição inicial."
            )
        ),
        Exercise(
            id = "ex_triceps_04",
            name = "Tríceps Coice com Halter",
            muscleGroup = "Tríceps",
            equipment = "Halter e Banco",
            description = "Isolamento com pico de contração no topo.",
            instructions = listOf(
                "Apoie uma mão no banco e incline o tronco.",
                "Mantenha o cotovelo paralelo ao tronco e estenda o braço para trás."
            )
        ),
        Exercise(
            id = "ex_triceps_05",
            name = "Tríceps Francês Unilateral com Halter",
            muscleGroup = "Tríceps",
            equipment = "Halter",
            description = "Foco na cabeça longa do tríceps na posição de alongamento.",
            instructions = listOf(
                "Segure o halter acima da cabeça com o braço estendido.",
                "Flexione o cotovelo descendo o peso por trás da cabeça.",
                "Estenda de volta."
            )
        ),
        Exercise(
            id = "ex_triceps_06",
            name = "Tríceps Mergulho no Banco",
            muscleGroup = "Tríceps",
            equipment = "Banco / Peso corporal",
            description = "Exercício prático de peso corporal para tríceps e peitoral.",
            instructions = listOf(
                "Apoie as mãos na borda do banco e pés no chão.",
                "Desça o quadril flexionando os cotovelos a 90°.",
                "Empurre de volta."
            )
        ),
        Exercise(
            id = "ex_triceps_07",
            name = "Paralelas para Tríceps",
            muscleGroup = "Tríceps",
            equipment = "Paralelas / Peso corporal",
            description = "Exercício calistênico pesado para tríceps.",
            instructions = listOf(
                "Mantenha o tronco ereto nas barras paralelas.",
                "Desça dobrando os cotovelos e empurre para subir."
            )
        ),
        Exercise(
            id = "ex_triceps_08",
            name = "Supino Fechado para Tríceps",
            muscleGroup = "Tríceps",
            equipment = "Barra e Banco Reto",
            description = "Supino com pegada fechada para grande transferência de carga no tríceps.",
            instructions = listOf(
                "Segure a barra com pegada na largura das ombros ou menor.",
                "Desça a barra no peito e empurre estendendo os braços."
            )
        ),
        Exercise(
            id = "ex_triceps_09",
            name = "Tríceps Francês na Polia Alta com Corda",
            muscleGroup = "Tríceps",
            equipment = "Cabo/polia",
            description = "Tensão constante na posição de alongamento do tríceps.",
            instructions = listOf(
                "Fique de costas para a polia alta segurando a corda acima da cabeça.",
                "Estenda os braços para a frente e para cima."
            )
        ),
        Exercise(
            id = "ex_triceps_10",
            name = "Tríceps na Máquina Articulada",
            muscleGroup = "Tríceps",
            equipment = "Máquina",
            description = "Movimento guiado e seguro para repetições concentradas.",
            instructions = listOf(
                "Sente-se na máquina e empurre os apoios para baixo.",
                "Retorne sentindo o tríceps alongar."
            )
        ),

        // --- QUADRÍCEPS E PERNAS (14) ---
        Exercise(
            id = "ex_pernas_01",
            name = "Agachamento Livre com Barra",
            muscleGroup = "Pernas",
            equipment = "Barra e Rack de Agachamento",
            description = "O rei dos exercícios de membros inferiores para quadríceps, glúteos e core.",
            instructions = listOf(
                "Posicione a barra sobre a parte superior do trapézio e retire do suporte.",
                "Mantenha os pés na largura dos ombros e adote postura neutra.",
                "Inicie o movimento dobrando o quadril e joelhos como se fosse sentar.",
                "Agache até a linha dos joelhos ou mais e empurre o chão para subir."
            )
        ),
        Exercise(
            id = "ex_pernas_02",
            name = "Agachamento Hack Machine",
            muscleGroup = "Pernas",
            equipment = "Máquina",
            description = "Agachamento em ângulo guiado para segurança e foco nos quadríceps.",
            instructions = listOf(
                "Apoie as costas e ombros nos suportes da máquina Hack.",
                "Destrave a máquina e desça dobrando os joelhos.",
                "Empurre a plataforma de volta."
            )
        ),
        Exercise(
            id = "ex_pernas_03",
            name = "Agachamento no Smith",
            muscleGroup = "Pernas",
            equipment = "Smith",
            description = "Permite ajuste do posicionamento dos pés com suporte guiado.",
            instructions = listOf(
                "Apoie a barra do Smith no trapézio e rode para destravar.",
                "Desça flexionando quadril e joelhos com controle.",
                "Empurre para subir."
            )
        ),
        Exercise(
            id = "ex_pernas_04",
            name = "Agachamento Búlgaro com Halteres",
            muscleGroup = "Quadríceps",
            equipment = "Halteres e Banco",
            description = "Trabalho unilateral de alto nível para quadríceps e glúteos.",
            instructions = listOf(
                "Apoie o peito de um pé no banco atrás de você.",
                "Agache com a perna da frente até a coxa ficar paralela ao chão.",
                "Empurre com o calcanhar da frente para subir."
            )
        ),
        Exercise(
            id = "ex_pernas_05",
            name = "Leg Press 45°",
            muscleGroup = "Quadríceps",
            equipment = "Máquina Leg Press",
            description = "Excelente opção para ganho de volume muscular nas pernas com suporte para as costas.",
            instructions = listOf(
                "Apoie as costas e cabeça no encosto e apoie os pés na plataforma na largura dos ombros.",
                "Destrave a trava de segurança e flexione os joelhos trazendo a carga até 90°.",
                "Empurre a plataforma de volta sem travar os joelhos no topo."
            )
        ),
        Exercise(
            id = "ex_pernas_06",
            name = "Cadeira Extensora",
            muscleGroup = "Quadríceps",
            equipment = "Máquina",
            description = "Isolamento puro do quadríceps.",
            instructions = listOf(
                "Ajuste o encosto e a almofada sobre a parte inferior das canelas.",
                "Estenda os joelhos até alinhar as pernas contraindo o quadríceps.",
                "Desça devagar."
            )
        ),
        Exercise(
            id = "ex_pernas_07",
            name = "Afundo com Halteres em Passada",
            muscleGroup = "Quadríceps",
            equipment = "Halteres",
            description = "Desenvolvimento de força dinâmica e equilíbrio em movimento.",
            instructions = listOf(
                "Segure um halter em cada mão e dê um passo à frente.",
                "Desça o joelho de trás em direção ao chão.",
                "Avance para o próximo passo continuamente."
            )
        ),
        Exercise(
            id = "ex_pernas_08",
            name = "Agachamento Sumô com Kettlebell",
            muscleGroup = "Pernas",
            equipment = "Kettlebell",
            description = "Base aberta focado em adutores, quadríceps e glúteos.",
            instructions = listOf(
                "Adote uma base bem aberta com pés apontados a 45°.",
                "Segure o kettlebell ao centro e agache profundo.",
                "Empurre com os calcanhares para subir."
            )
        ),
        Exercise(
            id = "ex_pernas_09",
            name = "Agachamento Sissy (Sissy Squat)",
            muscleGroup = "Quadríceps",
            equipment = "Peso corporal",
            description = "Isolamento de quadríceps em máxima extensão de joelho.",
            instructions = listOf(
                "Mantenha o corpo alinhado e projete os joelhos para a frente enquanto inclina o tronco para trás.",
                "Suba empurrando com a ponta dos pés e quadríceps."
            )
        ),
        Exercise(
            id = "ex_pernas_10",
            name = "Passada Estacionária com Barra",
            muscleGroup = "Quadríceps",
            equipment = "Barra Livre",
            description = "Trabalho contínuo de pernas sem necessidade de deslocamento no espaço.",
            instructions = listOf(
                "Com a barra no trapézio, dê um passo à frente e mantenha a posição.",
                "Flexione e estenda os joelhos sequencialmente."
            )
        ),
        Exercise(
            id = "ex_pernas_11",
            name = "Agachamento Goblet com Halter",
            muscleGroup = "Quadríceps",
            equipment = "Halter",
            description = "Ótimo para aprendizado de padrão de agachamento e postura ereta.",
            instructions = listOf(
                "Segure o halter junto ao peito com as duas mãos.",
                "Agache mantendo a coluna ereta e cotovelos entre os joelhos."
            )
        ),
        Exercise(
            id = "ex_pernas_12",
            name = "Adução de Quadril na Máquina",
            muscleGroup = "Pernas",
            equipment = "Máquina",
            description = "Fortalecimento dos adutores internos da coxa.",
            instructions = listOf(
                "Ajuste as hastes abertas na máquina adutora.",
                "Junte as pernas contraindo a parte interna da coxa.",
                "Abra devagar."
            )
        ),
        Exercise(
            id = "ex_pernas_13",
            name = "Abdução de Quadril na Máquina",
            muscleGroup = "Pernas",
            equipment = "Máquina",
            description = "Fortalecimento de glúteo médio e estabilizadores laterais de quadril.",
            instructions = listOf(
                "Sente-se e encoste as pernas nos apoios externos.",
                "Empurre as pernas para fora e retorne devagar."
            )
        ),
        Exercise(
            id = "ex_pernas_14",
            name = "Agachamento Isométrico na Parede",
            muscleGroup = "Quadríceps",
            equipment = "Peso corporal",
            description = "Trabalho isométrico de resistência de quadríceps.",
            instructions = listOf(
                "Apoie as costas na parede e agache até joelhos ficarem a 90°.",
                "Sustente a posição pelo tempo determinado."
            )
        ),

        // --- POSTERIOR DE COXA (8) ---
        Exercise(
            id = "ex_post_01",
            name = "Stiff com Barra",
            muscleGroup = "Posterior de Coxa",
            equipment = "Barra Livre",
            description = "Trabalho de alongamento e força na cadeia posterior.",
            instructions = listOf(
                "Segure a barra com pernas quase estendidas (joelhos levemente destravados).",
                "Projete o quadril para trás descendo a barra rente às pernas.",
                "Suba contraindo os isquiotibiais e glúteos."
            )
        ),
        Exercise(
            id = "ex_post_02",
            name = "Stiff com Halteres",
            muscleGroup = "Posterior de Coxa",
            equipment = "Halteres",
            description = "Variação de stiff com rotação natural dos halteres.",
            instructions = listOf(
                "Desça os halteres paralelos às pernas jogando o quadril para trás.",
                "Retorne erguendo o tronco."
            )
        ),
        Exercise(
            id = "ex_post_03",
            name = "Mesa Flexora",
            muscleGroup = "Posterior de Coxa",
            equipment = "Máquina",
            description = "Isolamento da musculatura posterior de coxa deitado.",
            instructions = listOf(
                "Deite-se de bruços e apoie a almofada sobre os tornozelos.",
                "Flexione os joelhos trazendo o rolo em direção aos glúteos.",
                "Desça o peso de forma controlada."
            )
        ),
        Exercise(
            id = "ex_post_04",
            name = "Cadeira Flexora",
            muscleGroup = "Posterior de Coxa",
            equipment = "Máquina",
            description = "Flexão de joelhos na posição sentada.",
            instructions = listOf(
                "Sente-se e trave o apoio de coxa.",
                "Puxe a trava com os calcanhares para baixo.",
                "Retorne estendendo os joelhos suavemente."
            )
        ),
        Exercise(
            id = "ex_post_05",
            name = "Flexão Nórdica (Nordic Hamstring)",
            muscleGroup = "Posterior de Coxa",
            equipment = "Peso corporal / Apoio",
            description = "Exercício excêntrico de altíssima intensidade para prevenção de lesões e força.",
            instructions = listOf(
                "Ajoelhe-se com os tornozelos fixados firmemente.",
                "Projete o corpo para a frente segurando a queda com os posteriores de coxa.",
                "Empurre suavemente o chão para retornar."
            )
        ),
        Exercise(
            id = "ex_post_06",
            name = "Levantamento Terra Romeno (RDL)",
            muscleGroup = "Posterior de Coxa",
            equipment = "Barra Livre",
            description = "Foco em padrão de dobradiça de quadril mantendo tensão no posterior.",
            instructions = listOf(
                "Inicie de pé e deslize a barra até logo abaixo dos joelhos.",
                "Retorne projetando a pelve para a frente."
            )
        ),
        Exercise(
            id = "ex_post_07",
            name = "Flexão de Coxa Unilateral em Pé",
            muscleGroup = "Posterior de Coxa",
            equipment = "Máquina",
            description = "Isolamento de uma perna por vez na máquina flexora em pé.",
            instructions = listOf(
                "Apoie a coxa no suporte e flexione o joelho elevando o calcanhar.",
                "Desça o peso com controle."
            )
        ),
        Exercise(
            id = "ex_post_08",
            name = "Bom Dia (Good Morning) com Barra",
            muscleGroup = "Posterior de Coxa",
            equipment = "Barra Livre",
            description = "Trabalha posterior de coxa, glúteos e eretores da espinha.",
            instructions = listOf(
                "Com a barra no trapézio, flexione o quadril levando o tronco à frente.",
                "Retorne à posição ereta."
            )
        ),

        // --- GLÚTEOS (8) ---
        Exercise(
            id = "ex_glut_01",
            name = "Elevação Pélvica (Hip Thrust)",
            muscleGroup = "Glúteos",
            equipment = "Barra e Banco",
            description = "Exercício de alta ativação muscular focado no glúteo máximo.",
            instructions = listOf(
                "Apoie a parte inferior das escápulas na borda de um banco reto.",
                "Posicione a barra acolchoada sobre a linha da pelve.",
                "Empurre o quadril para cima até alinhar coxas e tronco, contraindo forte os glúteos.",
                "Desça de forma controlada mantendo o queixo levemente recolhido."
            )
        ),
        Exercise(
            id = "ex_glut_02",
            name = "Elevação Pélvica Unilateral no Banco",
            muscleGroup = "Glúteos",
            equipment = "Peso corporal / Banco",
            description = "Trabalho unilateral para correção de assimetrias e estabilidade pélvica.",
            instructions = listOf(
                "Apoie no banco e eleve uma perna do chão.",
                "Faça a extensão do quadril empurrando com uma só perna."
            )
        ),
        Exercise(
            id = "ex_glut_03",
            name = "Glúteo Quatro Apoios (Caneleira)",
            muscleGroup = "Glúteos",
            equipment = "Caneleiras / Solo",
            description = "Isolamento tradicional no solo para glúteo máximo.",
            instructions = listOf(
                "Em posição de quatro apoios, eleve o calcanhar em direção ao teto.",
                "Contraia o glúteo no topo e desça sem tocar o joelho no chão."
            )
        ),
        Exercise(
            id = "ex_glut_04",
            name = "Glúteo no Cabo com Caneleira",
            muscleGroup = "Glúteos",
            equipment = "Cabo/polia",
            description = "Extensão de quadril no cabo com tensão constante.",
            instructions = listOf(
                "Prenda a caneleira na polia baixa.",
                "Chute a perna para trás mantendo a coluna firme."
            )
        ),
        Exercise(
            id = "ex_glut_05",
            name = "Abdução de Quadril no Cabo",
            muscleGroup = "Glúteos",
            equipment = "Cabo/polia",
            description = "Trabalho de glúteo médio na polia baixa.",
            instructions = listOf(
                "Fique de lado para a polia e afaste a perna lateralmente.",
                "Retorne suavemente."
            )
        ),
        Exercise(
            id = "ex_glut_06",
            name = "Coice de Glúteo na Máquina",
            muscleGroup = "Glúteos",
            equipment = "Máquina",
            description = "Extensão de quadril na máquina guiada.",
            instructions = listOf(
                "Posicione o pé na plataforma da máquina.",
                "Empurre a plataforma para trás contraindo o glúteo."
            )
        ),
        Exercise(
            id = "ex_glut_07",
            name = "Step Up no Banco com Halteres",
            muscleGroup = "Glúteos",
            equipment = "Halteres e Banco",
            description = "Subida no banco para ativação funcional de glúteos e quadríceps.",
            instructions = listOf(
                "Pise firme no centro do banco com um pé.",
                "Empurre com o calcanhar até ficar totalmente em pé sobre o banco.",
                "Desça suavemente."
            )
        ),
        Exercise(
            id = "ex_glut_08",
            name = "Agachamento Sumô no Banco com Halter",
            muscleGroup = "Glúteos",
            equipment = "Halter e Dois Bancos",
            description = "Permite amplitude extra de agachamento para máxima ativação de glúteos.",
            instructions = listOf(
                "Posicione os pés bem abertos um em cada banco.",
                "Agache profundo segurando o halter ao centro."
            )
        ),

        // --- PANTURRILHAS (6) ---
        Exercise(
            id = "ex_pant_01",
            name = "Gêmeos em Pé na Máquina",
            muscleGroup = "Panturrilhas",
            equipment = "Máquina",
            description = "Enfatiza o músculo gastrocnêmio com joelhos estendidos.",
            instructions = listOf(
                "Apoie a ponta dos pés na plataforma e ombros sob os apoios.",
                "Desça os calcanhares para alongar a panturrilha.",
                "Eleve os calcanhares o máximo possível no topo."
            )
        ),
        Exercise(
            id = "ex_pant_02",
            name = "Gêmeos Sentado na Máquina",
            muscleGroup = "Panturrilhas",
            equipment = "Máquina",
            description = "Focado no músculo sóleo com joelhos flexionados.",
            instructions = listOf(
                "Sente-se e posicione a almofada sobre as coxas.",
                "Eleve os calcanhares contraindo o sóleo."
            )
        ),
        Exercise(
            id = "ex_pant_03",
            name = "Elevação de Panturrilha no Smith",
            muscleGroup = "Panturrilhas",
            equipment = "Smith",
            description = "Elevação de calcanhares com barra guiada.",
            instructions = listOf(
                "Pise em um degrau sob a barra do Smith.",
                "Faça a flexão plantar completa."
            )
        ),
        Exercise(
            id = "ex_pant_04",
            name = "Elevação de Panturrilha no Leg Press",
            muscleGroup = "Panturrilhas",
            equipment = "Máquina Leg Press",
            description = "Treino de panturrilha com pernas estendidas no Leg Press.",
            instructions = listOf(
                "Posicione a ponta dos pés na borda inferior da plataforma.",
                "Empurre a plataforma com a ponta dos pés e desça alongando."
            )
        ),
        Exercise(
            id = "ex_pant_05",
            name = "Elevação de Panturrilha Unilateral em Pé",
            muscleGroup = "Panturrilhas",
            equipment = "Halter / Degrau",
            description = "Trabalho unilateral para equilíbrio e intensidade.",
            instructions = listOf(
                "Apoie a ponta de um pé em um degrau segurando um halter.",
                "Execute o movimento completo de subida e descida."
            )
        ),
        Exercise(
            id = "ex_pant_06",
            name = "Panturrilha Donkey (Burrinho)",
            muscleGroup = "Panturrilhas",
            equipment = "Máquina / Peso corporal",
            description = "Posição de tronco curvado que proporciona excelente alongamento e ativação.",
            instructions = listOf(
                "Incline o tronco a 90° e apoie o quadril no suporte.",
                "Eleve os calcanhares com amplitude total."
            )
        ),

        // --- ABDÔMEN (12) ---
        Exercise(
            id = "ex_abdom_01",
            name = "Abdominal Supra no Solo",
            muscleGroup = "Abdômen",
            equipment = "Peso corporal",
            description = "Exercício básico para a região superior do reto abdominal.",
            instructions = listOf(
                "Deite-se com joelhos dobrados e pés apoiados.",
                "Eleve os ombros do chão enrolando a coluna torácica.",
                "Retorne sem relaxar o abdômen completamente."
            )
        ),
        Exercise(
            id = "ex_abdom_02",
            name = "Abdominal Infra na Barra Fixa",
            muscleGroup = "Abdômen",
            equipment = "Barra Fixa / Peso corporal",
            description = "Trabalha a porção inferior do reto abdominal e flexores do quadril.",
            instructions = listOf(
                "Mantenha-se suspenso na barra fixa.",
                "Eleve as pernas ou joelhos até a linha do quadril sem balançar o corpo.",
                "Desça devagar."
            )
        ),
        Exercise(
            id = "ex_abdom_03",
            name = "Abdominal Remador",
            muscleGroup = "Abdômen",
            equipment = "Peso corporal",
            description = "Movimento calistênico completo conectando tronco e membros inferiores.",
            instructions = listOf(
                "Deite-se completamente estendido no chão.",
                "Sente-se abraçando os joelhos simultaneamente.",
                "Retorne à posição deitada estendida."
            )
        ),
        Exercise(
            id = "ex_abdom_04",
            name = "Abdominal no Cabo com Corda (Crunch)",
            muscleGroup = "Abdômen",
            equipment = "Cabo/polia",
            description = "Permite adicionar carga progressiva ao treino de abdominal.",
            instructions = listOf(
                "Ajoelhe-se de frente para a polia alta e segure a corda ao lado da cabeça.",
                "Flexione a coluna torácica puxando a carga para baixo.",
                "Retorne devagar sem usar os braços."
            )
        ),
        Exercise(
            id = "ex_abdom_05",
            name = "Prancha Frontal Isométrica",
            muscleGroup = "Abdômen",
            equipment = "Peso corporal",
            description = "Estabilização do core profundo e reto abdominal.",
            instructions = listOf(
                "Apoie os antebraços e pontas dos pés no chão.",
                "Mantenha a coluna neutra e o glúteo/abdômen contraídos pelo tempo estabelecido."
            )
        ),
        Exercise(
            id = "ex_abdom_06",
            name = "Prancha Lateral Isométrica",
            muscleGroup = "Abdômen",
            equipment = "Peso corporal",
            description = "Fortalecimento dos oblíquos e estabilização lateral.",
            instructions = listOf(
                "Deite-se de lado e apoie o antebraço no chão.",
                "Eleve o quadril mantendo o corpo em linha reta."
            )
        ),
        Exercise(
            id = "ex_abdom_07",
            name = "Abdominal Infra no Banco Inclinado",
            muscleGroup = "Abdômen",
            equipment = "Banco Inclinado",
            description = "Aumenta a gravidade no movimento infra-abdominal.",
            instructions = listOf(
                "Deite-se no banco inclinado segurando no apoio superior.",
                "Eleve o quadril enrolando a pelve para cima."
            )
        ),
        Exercise(
            id = "ex_abdom_08",
            name = "Roda Abdominal (Ab Wheel)",
            muscleGroup = "Abdômen",
            equipment = "Roda Abdominal",
            description = "Exercício de antiextensão de alta exigência muscular.",
            instructions = listOf(
                "Ajoelhe-se e segure a roda no chão.",
                "Role a roda para a frente mantendo o abdômen firme sem selar a lombar.",
                "Puxe a roda de volta."
            )
        ),
        Exercise(
            id = "ex_abdom_09",
            name = "Abdominal Bicicleta",
            muscleGroup = "Abdômen",
            equipment = "Peso corporal",
            description = "Trabalha reto abdominal e oblíquos simultaneamente.",
            instructions = listOf(
                "Deite-se no solo com as mãos atrás da cabeça.",
                "Toque o cotovelo direito no joelho esquerdo enquanto estende a perna direita, alternando os lados."
            )
        ),
        Exercise(
            id = "ex_abdom_10",
            name = "Torção Russa com Anilha (Russian Twist)",
            muscleGroup = "Abdômen",
            equipment = "Anilha / Peso corporal",
            description = "Trabalho rotacional de oblíquos.",
            instructions = listOf(
                "Sente-se com o tronco levemente inclinado e pés fora do chão.",
                "Gire a anilha de um lado para o outro do quadril."
            )
        ),
        Exercise(
            id = "ex_abdom_11",
            name = "Elevação de Pernas na Paralela",
            muscleGroup = "Abdômen",
            equipment = "Paralela / Máquina",
            description = "Apoio de antebraços na paralela com flexão de quadril e pelve.",
            instructions = listOf(
                "Apoie os antebraços e eleve as pernas estendidas à frente."
            )
        ),
        Exercise(
            id = "ex_abdom_12",
            name = "Vacuum Abdominal (Vácuo)",
            muscleGroup = "Abdômen",
            equipment = "Peso corporal",
            description = "Ativação do transverso do abdômen para afinar a cintura e postura.",
            instructions = listOf(
                "Solte todo o ar dos pulmões e puxe o umbigo para dentro em direção às costas.",
                "Segure a aspiração diafragmática pelo tempo determinado."
            )
        ),

        // --- CARDIO (10) ---
        Exercise(
            id = "ex_cardio_01",
            name = "Corrida na Esteira (Z2/Z3)",
            muscleGroup = "Cardio",
            equipment = "Esteira Aeróbica",
            description = "Treino contínuo para resistência cardiorrespiratória e gasto calórico.",
            instructions = listOf(
                "Ajuste velocidade e inclinação conforme a zona-alvo de frequência cardíaca.",
                "Mantenha a postura ereta e a passada fluída."
            )
        ),
        Exercise(
            id = "ex_cardio_02",
            name = "Caminhada com Inclinação na Esteira",
            muscleGroup = "Cardio",
            equipment = "Esteira Aeróbica",
            description = "Cardio de baixo impacto para articulações e alto consumo calórico.",
            instructions = listOf(
                "Ajuste a inclinação entre 8% e 12% e velocidade moderada.",
                "Mantenha os braços balançando sem segurar nas barras."
            )
        ),
        Exercise(
            id = "ex_cardio_03",
            name = "Bicicleta Ergométrica Horizontal",
            muscleGroup = "Cardio",
            equipment = "Bicicleta Ergométrica",
            description = "Cardio confortável com apoio para as costas.",
            instructions = listOf(
                "Ajuste a distância do banco para o pedal.",
                "Mantenha cadência constante de pedalada."
            )
        ),
        Exercise(
            id = "ex_cardio_04",
            name = "Air Bike / Spinning",
            muscleGroup = "Cardio",
            equipment = "Bicicleta Air Bike",
            description = "Cardio de alta intensidade envolvendo braços e pernas.",
            instructions = listOf(
                "Pedale e empurre/puxe as manoplas com ritmo forte."
            )
        ),
        Exercise(
            id = "ex_cardio_05",
            name = "Simulador de Escada (Stairmaster)",
            muscleGroup = "Cardio",
            equipment = "Escada Ergométrica",
            description = "Cardio de alta exigência metabólica com foco em glúteos e pernas.",
            instructions = listOf(
                "Suba os degraus mantendo o tronco ereto sem debruçar sobre o painel."
            )
        ),
        Exercise(
            id = "ex_cardio_06",
            name = "Elíptico / Transport",
            muscleGroup = "Cardio",
            equipment = "Elíptico",
            description = "Atividade de impacto zero para articulações.",
            instructions = listOf(
                "Movimente pernas e braços em sincronicidade no aparelho."
            )
        ),
        Exercise(
            id = "ex_cardio_07",
            name = "Remo Seco (Ergômetro)",
            muscleGroup = "Cardio",
            equipment = "Remo Ergômetro",
            description = "Exercício metabólico completo ativando 85% da musculatura corporal.",
            instructions = listOf(
                "Empurre com as pernas, incline o tronco e puxe com os braços.",
                "Inverta a sequência no retorno."
            )
        ),
        Exercise(
            id = "ex_cardio_08",
            name = "Corda Naval (Battle Rope)",
            muscleGroup = "Cardio",
            equipment = "Corda Naval",
            description = "Treino metabólico e de potência de membros superiores.",
            instructions = listOf(
                "Flexione levemente os joelhos e faça ondas alternadas rápidas com a corda."
            )
        ),
        Exercise(
            id = "ex_cardio_09",
            name = "Salto de Corda (Jumping Rope)",
            muscleGroup = "Cardio",
            equipment = "Corda",
            description = "Melhora a agilidade, coordenação e condicionamento.",
            instructions = listOf(
                "Salte na ponta dos pés girando a corda com os punhos."
            )
        ),
        Exercise(
            id = "ex_cardio_10",
            name = "Burpees Calistênicos",
            muscleGroup = "Cardio",
            equipment = "Peso corporal",
            description = "Exercício calistênico de alta intensidade agachando, fazendo flexão e saltando.",
            instructions = listOf(
                "Agache, jogue os pés para trás em posição de flexão, toque o peito no chão, volte os pés e salte."
            )
        )
    )
}
