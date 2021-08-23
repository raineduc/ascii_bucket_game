package bucket_game.components

import bucket_game.domain.Ball

class BallComponent(
                     override val gameObject: Ball,
                     override val renderer: Renderer[Ball]
                   ) extends Component[Ball]
